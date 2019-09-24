package com.andcreations.base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Base64 encoder/decoder.
 * 
 * @author Mikolaj Gucki
 */
public class Base64 {
	/**
	 * Encodes a 6-bit value into a Base64 value.
	 * 
	 * @param value The value to encode.
	 * @return The encoded Base64 value.
	 * @throws IllegalArgumentException if the value is invalid.
	 */
	private static int encode(int value) {
		if (value >= 0 && value <= 25) {
			return value + 'A';
		}

		if (value >= 26 && value <= 51) {
			return value + 'a' - 26;
		}

		if (value >= 52 && value <= 61) {
			return value + '0' - 52;
		}

		if (value == 62) {
			return '+';
		}
		if (value == 63) {
			return '/';
		}

		throw new IllegalArgumentException("Invalid Base64 value");
	}

	/**
	 * Encodes three bytes into four Base64 values.
	 * 
	 * @param byte0 The first byte.
	 * @param byte1 The second byte.
	 * @param byte2 The third byte.
	 * @return A 4-integer long array containing the Base64 values.
	 */
	private static int[] encode(int byte0,int byte1,int byte2) {
		int[] encoded = new int[4];

		encoded[0] = encode(byte0 >> 2);
		encoded[1] = encode(((byte0 & 3) << 4) | (byte1 >> 4));
		encoded[2] = encode(((byte1 & 15) << 2) | (byte2 >> 6));
		encoded[3] = encode(byte2 & 63);

		return encoded;
	}

	/**
	 * Encodes a stream of bytes into Base64 stream.
	 * 
	 * @param input The input stream.
	 * @param output The output stream.
	 * @throws IOException on I/O error.
	 */
	public static void encode(InputStream input,OutputStream output)
		throws IOException {
	// for each three bytes
		while (true) {
			int byte0 = input.read();
		// if the first byte is not given, stop
			if (byte0 == -1) {
				break;
			}

			int byte1 = input.read();
		// if the second byte is not given, pad with two characters
			if (byte1 == -1) {
				int[] encoded = encode(byte0,0,0);
				for (int index = 0; index < 2; index++) {
					output.write(encoded[index]);
				}
				output.write('=');
				output.write('=');

				break;
			}

			int byte2 = input.read();
		// if the third byte is not given, pad with one characte
			if (byte2 == -1) {
				int[] encoded = encode(byte0,byte1,0);
				for (int index = 0; index < 3; index++) {
					output.write(encoded[index]);
				}
				output.write('=');

				break;
			}

		// all three input bytes given
			int[] encoded = encode(byte0,byte1,byte2);
			for (int index = 0; index < 4; index++) {
				output.write(encoded[index]);
			}
		}
	}

	/**
	 * Decodes a Base64 value into a byte value.
	 * 
	 * @param value The base64 value.
	 * @return The decoded byte value.
	 * @throws IllegalArgumentException if the value is invalid.
	 */
	private static int decode(int value) {
		if (value >= 'A' && value <= 'Z') {
			return value - 'A';
		}

		if (value >= 'a' && value <= 'z') {
			return value - 'a' + 26;
		}

		if (value >= '0' && value <= '9') {
			return value - '0' + 52;
		}

		if (value == '+') {
			return 62;
		}
		if (value == '/') {
			return 63;
		}

		throw new IllegalArgumentException("Invalid Base64 value");
	}

	/**
	 * Encodes a Base64 stream into stream of bytes.
	 * 
	 * @param input The input stream.
	 * @param output The output stream.
	 * @throws IOException on I/O error.
	 */
	public static void decode(InputStream input,OutputStream output)
		throws IOException {
	// for each four bytes
		while (true) {
			int byte0 = input.read();
		// if end of the stream
			if (byte0 == -1) {
				break;
			}
			int byte1 = input.read();
			int byte2 = input.read();
			int byte3 = input.read();

		// there must be always four bytes
			if (byte1 == -1 || byte2 == -1 || byte3 == -1) {
				throw new IOException("The stream is not Base64 encoded");
			}

		// padded with two characters
			if (byte2 == '=') {
				output.write((decode(byte0) << 2) | (decode(byte1) >> 4));
				continue;
			}

		// padded with one character
			if (byte3 == '=') {
				output.write((decode(byte0) << 2) | (decode(byte1) >> 4));
				output.write((decode(byte1) << 4) | (decode(byte2) >> 2));

				continue;
			}

		// not padded
			output.write((decode(byte0) << 2) | (decode(byte1) >> 4));
			output.write((decode(byte1) << 4) | (decode(byte2) >> 2));
			output.write((decode(byte2) << 6) | decode(byte3));
		}
	}
	
	/**
	 * Decodes a Base64 string. 
	 * 
	 * @param inputString The input string.
	 * @return The decoded bytes.
	 * @throws IOException if the input string cannot be decoded.
	 */
	public static byte[] decode(String inputString) throws IOException {
	// input
		byte[] input = inputString.getBytes();
		InputStream inputStream = new ByteArrayInputStream(input);
		
	// output
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
	// decode
		decode(inputStream,outputStream);		
		return outputStream.toByteArray();
	}
	
	/**
	 * Encodes a byte array into a Base64 string.
	 * 
	 * @param inputData The data to encode.
	 * @return The encoded Base64 stream.
	 * @throws IOException if the input data cannot be encoded.
	 */
	public static String encode(byte[] inputData) throws IOException {
	// streams
		InputStream inputStream = new ByteArrayInputStream(inputData);		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
	// encode
		encode(inputStream,outputStream);
		
		return new String(outputStream.toByteArray());
	}
}