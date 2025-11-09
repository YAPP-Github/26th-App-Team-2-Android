package com.teambrake.brake.core.datastore.encryption

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

internal class CryptoData {
	private val cipher = Cipher.getInstance(TRANSFORMATION)
	private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
		load(null)
	}

	companion object {
		private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
		private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
		private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
		private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
	}

	private fun getKey(): SecretKey {
		// secret key 존재 여부 확인 후 반환, 없으면 새로 생성
		val existingKey = keyStore.getEntry("secret", null) as? KeyStore.SecretKeyEntry
		return existingKey?.secretKey ?: createKey()
	}

	private fun createKey(): SecretKey = KeyGenerator.getInstance(ALGORITHM).apply {
		init(
			KeyGenParameterSpec.Builder(
				"secret",
				KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
			).setBlockModes(BLOCK_MODE)
				.setEncryptionPaddings(PADDING)
				.build(),
		)
	}.generateKey()

	fun encrypt(bytes: ByteArray): ByteArray {
		// cipher 초기화 및 암호화 수행
		cipher.init(Cipher.ENCRYPT_MODE, getKey())
		val iv = cipher.iv
		val encrypted = cipher.doFinal(bytes)
		return iv + encrypted
	}

	fun decrypt(bytes: ByteArray): ByteArray? {
		// IV 와 암호화된 데이터 분리
		val iv = bytes.copyOfRange(0, cipher.blockSize)
		val data = bytes.copyOfRange(cipher.blockSize, bytes.size)
		cipher.init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
		return cipher.doFinal(data)
	}
}
