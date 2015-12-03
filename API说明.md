# 获得安全Code
* API: http://localhost:8080/service/smartool/api/v1/users/code
* Method: POST
* Request:

```json
{
		"mobileNum": 1370xxxx334,
		
}
```

* Response: 200

> 必须在页面使用图片验证码

# 注册
* API: http://localhost:8080/service/smartool/api/v1/users/register?code=xxxxx
* Method: POST
* Request: code=xxxxx

```json
{
		"mobileNum": 1370xxxx334,
		"name": "xxxx",
		"location": "杭州"
		"kids" : [
			{
				"name" : "xxxx",
				"schoolType": "0/1/2",
				"schoolName": “xxxx”,
				"age": 11,
				"gender": 1,
				"avatarUrl": "xxxxxx"
			}
		]
}
```

> schoolType
> * 0: 未上
> * 1: 幼儿园
> * 2: 小学
> Gender
> * 1: male
> * 2: female

# Avatar
* API: http://localhost:8080/service/smartool/api/v1/users/{userId}/kids/{kidId}/avatar
* Method: POST
* content-type: image/jpeg
* Request: base64 binary data


# 登陆
* API: http://localhost:8080/service/smartool/api/v1/users/login
* Method: POST
* Request

```json
{
	"mobileNum" : 1111111,
	"code" : 123414
}
```
> 使用Security Code
> 必须在页面使用图片验证码

# Cover页面／赛事排名
### 获得Cover 列表
* API: http://localhost:8080/service/smartool/api/v1/users/me/covers?start=0&limit=10
* Method：GET

```json
	{
        "id": "b088e397-c866-44de-ae74-1e3385b02c25",
        "credit": 100,
        "totalUserCredit": 150,
        "siteId": "4474ed41-b979-474b-b398-34c3079fc7dc",
        "eventTypeId": "cbedb2a5-907c-43e2-9463-a7529dca669b",
        "name": "邵小华",
        "centerLogoIcon": "",
        "fatestTime": 61,
        "totalAttendTimes": 2,
        "trophyList": [
            {
                "stage": 1,
                "roundLevel": 0,
                "roundLevelName": null,
                "roundId": null,
                "rank": 1
            }
        ],
        "kid": {
            "createdTime": 1442498448000,
            "lastModifiedTime": 1444835009000,
            "id": "46307c40-9b45-475f-b1f2-d452943190cf",
            "name": "方弘毅",
            "schoolType": 2,
            "schoolName": null,
            "userId": "b088e397-c866-44de-ae74-1e3385b02c25",
            "avatarUrl": "http://img.ismartool.cn/46307c40-9b45-475f-b1f2-d452943190cf.png?ts=1444835009014",
            "age": 0,
            "gender": 0,
            "firstTimeAttendEvent": 0,
            "likes": 0
        },
        "type": "grade"
    }
```

> 获得当前用户(me)同一类赛事类型的所有小孩的成绩列表
> 如果返回的type＝grade，表示返回的cover是某一用户的一个小孩的成绩
> 如果返回的type＝ad，表示返回的cover是广告
> start, limit: 用于分页
> credit：单个小孩的积分统计（决定排名，用于显示）
> totalUserCredit: 该用户所有小孩积分的统计
> kid: 小孩信息
> trophyList：获得奖杯的历史
> 日冠军：stage=1, roundLevel=3, rank=1
> 日亚军：stage=1, roundLevel=3, rank=2
> 日季军：stage=1, roundLevel=3, rank=3
> 奖杯数字又日冠／亚／季军的数量决定
> totalAttendTimes: 参加次数
> fatestTime: 最快时间（秒）
> centerLogoIcon: 目前预留字段
> Likes: Total Likes
> kid.likes: likes per kid




# 获得用户
* API: http://localhost:8080/service/smartool/api/v1/users/{userId}

```json
{
    "createdTime": 1442498397000,
    "lastModifiedTime": 1444353560000,
    "id": "b088e397-c866-44de-ae74-1e3385b02c25",
    "name": "邵小华",
    "mobileNum": "13665852966",
    "location": "杭州",
    "roleId": "0",
    "siteId": null,
    "kids": [
        {
            "createdTime": 1442498448000,
            "lastModifiedTime": 1444835009000,
            "id": "46307c40-9b45-475f-b1f2-d452943190cf",
            "name": "方弘毅",
            "schoolType": 2,
            "schoolName": null,
            "userId": "b088e397-c866-44de-ae74-1e3385b02c25",
            "avatarUrl": "http://img.ismartool.cn/46307c40-9b45-475f-b1f2-d452943190cf.png?ts=1444835009014",
            "age": 0,
            "gender": 0,
            "firstTimeAttendEvent": 0,
            "coverVideoLink": null,
            "likes": 0
        },
        {
            "createdTime": 1443624301000,
            "lastModifiedTime": 1443624301000,
            "id": "c02c4051-8499-4b74-ae8b-456a803469f8",
            "name": "测试1",
            "schoolType": 0,
            "schoolName": "幼儿园1",
            "userId": "b088e397-c866-44de-ae74-1e3385b02c25",
            "avatarUrl": null,
            "age": 0,
            "gender": 0,
            "firstTimeAttendEvent": 0,
            "coverVideoLink": null
        }
    ],
    "status": 0,
    "credit": 150,
    "goldenMedal": 0,
    "silverMedal": 0,
    "bronzeMedal": 3,
    "idp": 0,
    "likes": 0,
    "maxTeamMemberSize": 15
}
```
> userId: 从covers列表中获得
> likes: 获赞次数
> firstTimeAttendEvent: 第一次参加比赛的时间
> 名字：kid.name
> avatarUrl：用户头像
> coverVideoLink: Video链接

# 轨迹

* API: http://localhost:8080/service/smartool/api/v1/users/1b2b55bd-d716-4ec1-ab3d-78f2c7366392/kids/2b2aab04-73a4-4ec1-b1b5-2e68269bf316/tracks?start=0&limit=100
> Request: start, limit, 用于分页

``` json

[
    {
        "eventSeq": 24,
        "eventTime": 1446651300000,
        "eventId": "e9415c4d-42fd-462c-888a-8814ea9cc93d",
        "videoLink": null,
        "userId": "1b2b55bd-d716-4ec1-ab3d-78f2c7366392",
        "kidId": "2b2aab04-73a4-4ec1-b1b5-2e68269bf316",
        "achievementList": [
            {
                "stage": 1,
                "roundLevel": 1,
                "rank": 3,
                "type": "trophy"
            },
            {
                "stage": 1,
                "roundLevel": 3,
                "rank": 3,
                "type": "trophy"
            },
            {
                "stage": 1,
                "roundLevel": 3,
                "rank": 3,
                "type": "trophy"
            },
            {
                "stage": 1,
                "roundLevel": 1,
                "rank": 3,
                "type": "trophy"
            },
            {
                "stage": 1,
                "roundLevel": 2,
                "rank": 1,
                "type": "trophy"
            },
            {
                "stage": 1,
                "roundLevel": 2,
                "rank": 1,
                "type": "trophy"
            }
        ]
    }
]
```
> eventTime： 赛事时间
> credit：单次获得的积分
> videoLink：该赛事的video，可能为空
> rank: 对应奖杯
> score：秒

＃获得个人信息

* API: http://localhost:8080/service/smartool/api/v1/users/me
* METHOD: GET

```json
{
    "createdTime": 1446301526000,
    "lastModifiedTime": 1448120700000,
    "id": "1b2b55bd-d716-4ec1-ab3d-78f2c7366392",
    "name": "zfl",
    "mobileNum": "18057181866",
    "location": "杭州",
    "roleId": "0",
    "siteId": null,
    "kids": [
        {
            "createdTime": 1446648901000,
            "lastModifiedTime": 1448372275000,
            "id": "0f3d6550-8048-40a9-bf2c-1b730db0ca3c",
            "name": "555",
            "schoolType": 2,
            "schoolName": null,
            "userId": "1b2b55bd-d716-4ec1-ab3d-78f2c7366392",
            "avatarUrl": null,
            "age": 0,
            "gender": 0,
            "firstTimeAttendEvent": 0,
            "coverVideoLink": null
        }]
}
```

> kid.name: 车手昵称
> firstTimeAttendEvent: 计算驾龄


# 二维码
* API: /users/me/qrcode
* METHOD: GET
* RETURN: image/png

# Increment Likes

* API: http://localhost:8080/service/smartool/api/v1/users/{userId}/kids/{kidId}/likes
* METHOD: PUT
* No return result

# Get Likes
* API：等同于 获得用户

> 当increment用户后，不要call 获得用户，请在页面自动加数据。忽略不同步
> 当每次打开页面后，获取likes


# Get Teams
* API: http://localhost:8080/service/smartool/api/v1/users/{userId}/kids/{kidId}/teams
* Method: GET

```json
	[
    {
        "createdTime": 1448372255000,
        "lastModifiedTime": 1448464066000,
        "id": "0c552d1e-82c8-4889-95ba-adc3c286031d",
        "name": "Test",
        "ownerId": null,
        "tp": 0,
        "pid": null,
        "size": 10,
        "minSize": 5
    }
]
```

# Create team
* API: http://localhost:8080/service/smartool/api/v1/teams
* METHOD: POST
* Request

```json
{
        "createdTime": 1446648886000,
        "lastModifiedTime": 1448372275000,
        "name": "Team2",
        "size": 5
      }
```

* Response

```json
	{
  "createdTime": 1446648886000,
  "lastModifiedTime": 1448372275000,
  "id": "71fb82ae-dd89-4e48-880c-ffdbb5e2b239",
  "name": "Team2",
  "ownerId": "5180050e-dc77-4df4-b029-46092ae0a25d",
  "tp": 0,
  "pid": null,
  "size": 5,
  "minSize": 0
}
```

> OwnerId: userID who login and create team


# Join team
* API: http://localhost:8080/service/smartool/api/v1/teams/{teamId}/members
* METHOD: POST
* Request

```json
{
    "id": "0f3d6550-8048-40a9-bf2c-1b730db0ca3c",
    "name": "555",
    "schoolType": 2,
    "userId": "1b2b55bd-d716-4ec1-ab3d-78f2c7366392"
}
```

* Response: HTTP COde: 200


# Leave team
* API: http://localhost:8080/service/smartool/api/v1/teams/{teamId}/members/{memberId}
* METHOD: DELETE
* Response: 200

# Found Recent Event
* API: http://localhost:8080/service/smartool/api/v1//events/recent?start=2015-09-19 08:00:00&end=2015-12-19 08:00:00
* Method: GET

```json
	{
    "createdTime": 1444492725000,
    "lastModifiedTime": 1445684840000,
    "id": "200e500b-4260-4edc-b931-0c8ca8b56370",
    "name": "A 10-10-2015 uuuuu",
    "eventTime": 1444457400000,
    "quota": 16,
    "attendees": []
   }
```
# Join Event
* API: http://localhost:8080/service/smartool/api/v1/events/{eventId}/enroll
* Method: POST
* Request: 

```json
{
        "kidId": "70e6b714-83c5-4811-9eef-3de6b32edae4",
        "userId": "15203ea3-17e1-40fb-9141-a2991d2f3ce9",
        "teamId": "0c552d1e-82c8-4889-95ba-adc3c286031d"
      }
```

* Reponse: Attendee

```json
{
  "createdTime": 1448372325000,
  "lastModifiedTime": 1448717867000,
  "id": "9aa2e882-4153-44bb-b84e-21868e781081",
  "kidName": "4",
  "kidId": "70e6b714-83c5-4811-9eef-3de6b32edae4",
  "userId": "15203ea3-17e1-40fb-9141-a2991d2f3ce9",
  "eventId": "835c1d94-2241-485b-9202-7ec0aa6b31fe",
  "avatarUrl": null,
  "tagId": null,
  "tag": null,
  "score": 0,
  "rank": 0,
  "seq": 72,
  "teamName": null,
  "teamId": null,
  "schoolType": 0,
  "schoolName": null,
  "nextRoundId": null,
  "status": 1,
  "attendeeNotifyTimes": null,
  "roundId": "951d936c-9f11-4648-afbb-b0ee530e31a1",
  "roundShortName": null,
  "roundLevelName": null,
  "roundLevel": 0,
  "videoLink": null
}
```


