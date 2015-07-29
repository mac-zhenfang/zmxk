package com.heatMap.Timer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HeatMapOfflineTask
{
  private static final ILog logger = LogFactory.getLog("ForHeatMapLogger");
  
  public static void main(String[] args)
    throws IOException, JSONException
  {
    logger.info("---------------HeatMap OfflineTask Start---------------");
    Long start = Long.valueOf(System.currentTimeMillis());
    ApplicationContext context = new ClassPathXmlApplicationContext(
      "order-application.xml");
    RedisService redisService = (RedisService)context.getBean(RedisService.class);
    BufferedReader br = new BufferedReader(new FileReader(
      args[0]));
    
    String data = "!";
    String timeSeg = TimeUtil.calCurrentTimeSeg();
    Integer mX = Integer.valueOf(0);
    Integer mY = Integer.valueOf(0);
    Double maxVal = Double.valueOf(0.0D);
    Integer priCity = null;
    
    double[] threshold = new double[3];
    threshold[0] = 0.3D;
    threshold[1] = 0.6D;
    threshold[2] = 0.99D;
    threshold[0] = Double.parseDouble(args[2]);
    threshold[1] = Double.parseDouble(args[3]);
    threshold[2] = Double.parseDouble(args[4]);
    
    Integer expire = Integer.valueOf(3600);
    expire = Integer.valueOf(Integer.parseInt(args[1]));
    
    double[][] map = (double[][])null;
    while (data != null)
    {
      data = br.readLine();
      if ((data == null) || (data == "")) {
        break;
      }
      String[] tokens = data.split("\t");
      
      Integer cityId = Integer.valueOf(Integer.parseInt(tokens[0]));
      if (cityId != priCity)
      {
        if (priCity != null)
        {
          String result = IsoContour.fun(map, mX.intValue(), mY.intValue(), threshold);
          
          calRedis(redisService, result, priCity, timeSeg, expire);
          
          String fkey = "if_finished_" + priCity + "_" + timeSeg;
          redisService.set(fkey, "1", 3600);
        }
        priCity = cityId;
        mX = Integer.valueOf(Integer.parseInt(redisService
          .get("city_index_x_" + cityId)));
        mY = Integer.valueOf(Integer.parseInt(redisService
          .get("city_index_y_" + cityId)));
        map = new double[mY.intValue() + 1][mX.intValue() + 1];
        maxVal = Double.valueOf(0.0D);
      }
      Integer x = Integer.valueOf(Math.round(Float.parseFloat(tokens[2])));
      Integer y = Integer.valueOf(Math.round(Float.parseFloat(tokens[3])));
      map[y.intValue()][x.intValue()] = Double.parseDouble(tokens[4]);
      if (map[y.intValue()][x.intValue()] > maxVal.doubleValue()) {
        maxVal = Double.valueOf(map[y.intValue()][x.intValue()]);
      }
    }
    String result = IsoContour.fun(map, mX.intValue(), mY.intValue(), threshold);
    calRedis(redisService, result, priCity, timeSeg, expire);
    
    br.close();
    
    String fkey = "if_finished_" + priCity + "_" + timeSeg;
    redisService.set(fkey, "1", 3600);
    
    Long end = Long.valueOf(System.currentTimeMillis());
    logger.info("--------------Mission Complete! Duration is " + (
      end.longValue() - start.longValue()) + "-------------");
    System.exit(0);
  }
  
  private static void calRedis(RedisService redis, String result, Integer city, String timeSeg, Integer expire)
    throws JSONException
  {
    JSONObject json = new JSONObject(result);
    JSONArray levelValue = json.getJSONArray("level_value");
    JSONArray contours = json.getJSONArray("contours");
    Integer num = Integer.valueOf(contours.length());
    System.out.println(num);
    for (int i = 0; i < contours.length(); i++)
    {
      System.out.println(i);
      String contourId = contours.getJSONObject(i).get("contour_id")
        .toString();
      String pathId = contours.getJSONObject(i).get("path_id").toString();
      JSONArray points = contours.getJSONObject(i).getJSONArray("points");
      Integer tot = Integer.valueOf(points.length());
      String key = city + "$" + timeSeg + "$" + contourId + "$" + pathId;
      logger.info(key + ": " + tot);
      for (int j = 0; j < points.length(); j++)
      {
        JSONObject p = points.getJSONObject(j);
        Double x = Double.valueOf(Double.parseDouble(p.get("x").toString()));
        Double y = Double.valueOf(Double.parseDouble(p.get("y").toString()));
        Integer x0 = Integer.valueOf((int)Math.floor(x.doubleValue()));
        Integer y0 = Integer.valueOf((int)Math.floor(y.doubleValue()));
        Integer x1 = Integer.valueOf((int)Math.ceil(x.doubleValue()));
        Integer y1 = Integer.valueOf((int)Math.ceil(y.doubleValue()));
        String mapRes = redis.get("index%" + city + "%" + x0 + "%" + y0);
        Double lat0 = Double.valueOf(Double.parseDouble(mapRes.split(",")[0]));
        Double lng0 = Double.valueOf(Double.parseDouble(mapRes.split(",")[1]));
        mapRes = redis.get("index%" + city + "%" + x1 + "%" + y1);
        Double lat1 = Double.valueOf(Double.parseDouble(mapRes.split(",")[0]));
        Double lng1 = Double.valueOf(Double.parseDouble(mapRes.split(",")[1]));
        
        Double lat = Double.valueOf(lat0.doubleValue() + (lat1.doubleValue() - lat0.doubleValue()) * (y.doubleValue() - y0.intValue()));
        Double lng = Double.valueOf(lng0.doubleValue() + (lng1.doubleValue() - lng0.doubleValue()) * (x.doubleValue() - x0.intValue()));
        
        String index = p.get("index").toString();
        
        String value = lat + "%" + lng + "%" + index;
        
        redis.addToSet(key, value);
      }
      redis.expire(key, expire.intValue());
    }
  }
}
