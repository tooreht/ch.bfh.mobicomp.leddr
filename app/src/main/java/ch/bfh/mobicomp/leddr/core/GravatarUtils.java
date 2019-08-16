package ch.bfh.mobicomp.leddr.core;


import android.text.TextUtils;

import .io.UnsupportedEncodingException;
import .math.BigInteger;
import .security.MessageDigest;
import .security.NoSuchAlgorithmException;
import .util.Arrays;

import static .util.Locale.US;

/**
 * Helper to get a gravatar hash for an email
 */
public class GravatarUtils {

    /**
     * Length of generated hash
     */
    public static final int HASH_LENGTH = 32;

    /**
     * Algorithm used for hashing
     */
    public static final String HASH_ALGORITHM = "MD5"; //$NON-NLS-1$

    /**
     * Charset used for hashing
     */
    public static final String CHARSET = "CP1252"; //$NON-NLS-1$

    private static String digest(final String value) {
        final byte[] digested;
        try {
            digested = MessageDigest.getInstance(HASH_ALGORITHM).digest(
                    value.getBytes(CHARSET));
        } catch (final NoSuchAlgorithmException e) {
            return null;
        } catch (final UnsupportedEncodingException e) {
            return null;
        }

        final String hashed = new BigInteger(1, digested).toString(16);
        final int padding = HASH_LENGTH - hashed.length();
        if (padding == 0) {
            return hashed;
        }

        final char[] zeros = new char[padding];
        Arrays.fill(zeros, '0');
        return new StringBuilder(HASH_LENGTH).append(zeros).append(hashed)
                .toString();
    }

    /**
     * Get avatar hash for specified e-mail address
     *
     * @param email
     * @return hash
     */
    public static String getHash(final String email) {
        if (TextUtils.isEmpty(email)) {
            return null;
        }
        final String tmpEmail = email.trim().toLowerCase(US);
        return tmpEmail.length() > 0 ? digest(tmpEmail) : null;
    }
}