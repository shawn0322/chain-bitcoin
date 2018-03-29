package com.tunion.bitcoincash;

import com.tunion.cores.utils.ISOUtil;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.LegacyAddress;
import org.bitcoinj.params.TestNet3Params;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static com.tunion.bitcoincash.BitcoinCashAddressFormatter.toCashAddress;
import static org.junit.Assert.*;

/**
 * Copyright (c) 2018 Tobias Brandt
 * 
 * Distributed under the MIT software license, see the accompanying file LICENSE
 * or http://www.opensource.org/licenses/mit-license.php.
 */
public class BitcoinCashAddressFormatterTests {

	Random random = new Random(94924425);

	@Test
	public void testCreateBitcoinCashAddressAndValidate() {
		byte[] sha256hash160 = new byte[] { 25, 94, 4, (byte) 141, 93, (byte) 189, 92, 111, 38, 2, 82, (byte) 185,
				(byte) 229, 9, 17, 63, (byte) 134, (byte) 217, 124, 42 };

		String cashAddress = toCashAddress(BitcoinCashAddressType.P2PKH, sha256hash160,
				MoneyNetwork.MAIN);

		assertEquals("bitcoincash:qqv4upydtk74cmexqfftnegfzylcdktu9gdgnyngsj", cashAddress);
		assertTrue(BitcoinCashAddressFormatter.isValidCashAddress(cashAddress, MoneyNetwork.MAIN));
		assertTrue(BitcoinCashAddressFormatter
				.isValidCashAddress(cashAddress.split(BitcoinCashAddressFormatter.SEPARATOR)[1], MoneyNetwork.MAIN));

		assertFalse(BitcoinCashAddressFormatter
				.isValidCashAddress("bitcoincash:Qqv4upydtk74cmexqfftnegfzylcdktu9gdgnyngsj", MoneyNetwork.MAIN));
		assertTrue(BitcoinCashAddressFormatter
				.isValidCashAddress("bitcoincash:QQV4UPYDTK74CMEXQFFTNEGFZYLCDKTU9GDGNYNGSJ", MoneyNetwork.MAIN));

		byte[] randomHash = new byte[20];
		for (int i = 0; i < 20000; i++) {
			random.nextBytes(randomHash);

			String randomCashAddress = toCashAddress(BitcoinCashAddressType.P2PKH,
					randomHash, MoneyNetwork.MAIN);
			assertTrue(BitcoinCashAddressFormatter.isValidCashAddress(randomCashAddress, MoneyNetwork.MAIN));

			String randomTestCashAddress = toCashAddress(BitcoinCashAddressType.P2PKH,
					randomHash, MoneyNetwork.TEST);
			assertTrue(BitcoinCashAddressFormatter.isValidCashAddress(randomTestCashAddress, MoneyNetwork.TEST));
		}
	}

	@Test
	public void testChecksumTestsFromSpec() {
		assertTrue(BitcoinCashAddressFormatter.isValidCashAddress("prefix:x64nx6hz", MoneyNetwork.MAIN));
		assertTrue(BitcoinCashAddressFormatter.isValidCashAddress("p:gpf8m4h7", MoneyNetwork.MAIN));
		assertTrue(BitcoinCashAddressFormatter
				.isValidCashAddress("bitcoincash:qpzry9x8gf2tvdw0s3jn54khce6mua7lcw20ayyn", MoneyNetwork.MAIN));
		assertFalse(
				BitcoinCashAddressFormatter.isValidCashAddress("bchtest:testnetaddress4d6njnut", MoneyNetwork.MAIN));
		assertTrue(BitcoinCashAddressFormatter.isValidCashAddress("bchtest:testnetaddress4d6njnut", MoneyNetwork.TEST));
		assertTrue(BitcoinCashAddressFormatter
				.isValidCashAddress("bchreg:555555555555555555555555555555555555555555555udxmlmrz", MoneyNetwork.MAIN));
	}

	@Test
	public void testDecoding() {
		BitcoinCashAddressDecodedParts cashAddress = BitcoinCashAddressFormatter
				.decodeCashAddress("bchtest:qrnuutt8ckm7sptrsh8eh7d7gv3rxwh9su7wy896rh", MoneyNetwork.TEST);

		System.out.println(ISOUtil.byte2hex(cashAddress.getHash()));

		Address address= LegacyAddress.fromPubKeyHash(TestNet3Params.get(),cashAddress.getHash());

		System.out.println(address.toString());

		BitcoinCashAddressDecodedParts decodedCashAddress = BitcoinCashAddressFormatter
				.decodeCashAddress("bitcoincash:qpt387qt6882v52ujj5aqxzx9tn7zvkqrvy8euqwhe", MoneyNetwork.MAIN);
		byte[] expectedHash = new byte[] { 87, 19, -8, 11, -47, -50, -90, 81, 92, -108, -87, -48, 24, 70, 42, -25, -31,
				50, -64, 27 };

		assertEquals(BitcoinCashAddressType.P2PKH, decodedCashAddress.getAddressType());
		assertEquals("bitcoincash", decodedCashAddress.getPrefix());
		assertTrue(Arrays.equals(expectedHash, decodedCashAddress.getHash()));

		byte[] randomHash = new byte[20];
		for (int i = 0; i < 2000; i++) {
			random.nextBytes(randomHash);

			String p2pkhCashAddress = toCashAddress(BitcoinCashAddressType.P2PKH,
					randomHash, MoneyNetwork.MAIN);

			decodedCashAddress = BitcoinCashAddressFormatter.decodeCashAddress(p2pkhCashAddress, MoneyNetwork.MAIN);
			assertEquals(BitcoinCashAddressType.P2PKH, decodedCashAddress.getAddressType());
			assertEquals("bitcoincash", decodedCashAddress.getPrefix());
			assertTrue(Arrays.equals(randomHash, decodedCashAddress.getHash()));

			String p2shCashAddress = toCashAddress(BitcoinCashAddressType.P2SH, randomHash,
					MoneyNetwork.TEST);

			decodedCashAddress = BitcoinCashAddressFormatter.decodeCashAddress(p2shCashAddress, MoneyNetwork.TEST);
			assertEquals(BitcoinCashAddressType.P2SH, decodedCashAddress.getAddressType());
			assertEquals("bchtest", decodedCashAddress.getPrefix());
			assertTrue(Arrays.equals(randomHash, decodedCashAddress.getHash()));
		}
	}

	@Test
	public void testEncoding()
	{
		Address address=LegacyAddress.fromBase58(TestNet3Params.get(),"n2edNpb4wrt8VEoJDBd5TdjYdBvJGh65YA");
		String addrStr=BitcoinCashAddressFormatter.toCashAddress(BitcoinCashAddressType.P2PKH,address.getHash(),MoneyNetwork.TEST);
		System.out.println(addrStr);
	}
}
