package com.smartool.service;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;

import com.smartool.service.controller.BaseController;

public class Encrypter {
	private static Logger logger = Logger.getLogger(BaseController.class);

	private String algorithm = "AES";
	private String secureKey = "JJ7kKLiXxkTWZFjl43+X9A==";
	private byte[] key = null;

	public Encrypter(String algorithm, String secureKey) {
		this.algorithm = algorithm;
		this.secureKey = secureKey;
		key = Base64.getDecoder().decode(this.secureKey);
	}

	public String encrypt(byte[] data) {
		// Instantiate the cipher
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(key, algorithm);
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

			byte[] encrypted = cipher.doFinal(data);
			return DatatypeConverter.printHexBinary(encrypted);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

	public byte[] decrypt(String encrypted) {
		try {
			byte[] tmp = DatatypeConverter.parseHexBinary(encrypted);

			SecretKeySpec skeySpec = new SecretKeySpec(key, algorithm);
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);

			return cipher.doFinal(tmp);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new byte[0];
		}
	}
}
