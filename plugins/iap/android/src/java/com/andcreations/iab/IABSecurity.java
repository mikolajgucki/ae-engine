package com.andcreations.iab;

import java.io.IOException;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import com.andcreations.base64.Base64;

/**
 * @author Mikolaj Gucki
 */
class IABSecurity {
	/**
	 * The key factory algorithm.
	 */
	private static final String KEY_FACTORY_ALGORITHM = "RSA";
	
	/**
	 * The signature algorithm.
	 */
	private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";	
	
	/**
	 * Verifies the signature of the data sent in the purchase response intent.
	 * 
	 * @param publicKey The application public key.
	 * @param inAppSignature The signature.
	 * @param inAppSignedData The signed data.
	 * @return <tt>true</tt> if the signature is correct, <tt>false</tt>
	 *   otherwise.
	 * @throws SignatureVerificationException if the signature cannot be
	 *   verified.
	 */
	static boolean verifySignature(String publicKey,String inAppSignature,
		String inAppSignedData) throws SignatureVerificationException {
	// 
		String encodedKey = publicKey;
		byte[] decodedKey = null;
		
	// decode the Base64-encoded key
		try {
			decodedKey = Base64.decode(encodedKey);
		} catch (IOException exception) {
			throw new SignatureVerificationException(
				"Could not decode the Base64-encoded public key",exception);
		}
		
	// create the key
		PublicKey key = null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(
				KEY_FACTORY_ALGORITHM);
			key = keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
		} catch (NoSuchAlgorithmException exception) {
			throw new SignatureVerificationException("Key factory algorithm " +
				KEY_FACTORY_ALGORITHM + " unavailable",exception);
		} catch (InvalidKeySpecException exception) {
			throw new SignatureVerificationException(
				"Invalid public key specified",exception);
		}
		
	// verify
		Signature signature = null;
		try {
			signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initVerify(key);
			signature.update(inAppSignedData.getBytes());
			if (signature.verify(Base64.decode(inAppSignature)) == false) {
				return false;
			}
		} catch (NoSuchAlgorithmException exception) {
			throw new SignatureVerificationException("Signature algorithm " +
				KEY_FACTORY_ALGORITHM + " unavailable",exception);
		} catch (InvalidKeyException exception) {
			throw new SignatureVerificationException(
				"Invalid public key specified",exception);
		} catch (SignatureException exception) {
			throw new SignatureVerificationException(
				"Signature error",exception);			
		} catch (IOException exception) {
			throw new SignatureVerificationException(
				"Could not decode the Base64-encoded signature",exception);			
		}		
		
		return true;
	}
}
