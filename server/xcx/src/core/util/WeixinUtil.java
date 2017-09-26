package core.util;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Base64;

/**
 * 微信小程序信息获取
 * @author eyeshot
 * @Date 2017年2月16日 11:56:08
 */
public class WeixinUtil {
	
	static Logger logger = Logger.getLogger(WeixinUtil.class);
	
	public static void main(String[] args) 
	{
		String data = "XtSQr8Qbx5uoqnTvhswsNSySGefP1L4/89Dj3cC+nDF1qxxx2crqTKwBQOg60T8Gx85SmXlxKQ1BP5xApCbS19pan8xt9Dots/tmo3xOaZwsaMgx65ywwwFFMGDOSkaqyKvJi+pIC7dHC3FLW/zPNCABNSOiXK81Jq0BnZWUbG1lx9Kzu/IsG/w3kYvdMTAxlutj79g/F1P7s5T+45LA0zWKRzfVtHmQDhlrPWvrMV1igiutPRBz63VI6MhNNkstxLP3kgDxqbSOhbwiPg/6MDa/wJ04WJOeQeWg/+dYfYplRGn965c/gxw5WZZvxT0Rj4J88OFKAfOWzVNsd54RS77Wy6/azIS36+zj0xMI2WJ/XyJdQDNcI95neY5KX+D6Zo7dIT/TihdHoZI82N+lUmH4ujHKXuwSzVOrFlhNTBc2syG0e4LDqwkcqloVghgAvS4AOCiyDOJZ2raQ/WHWUpzM0ckHJqvQo94HCvcyowU=";
		String iv = "YIAln0gtZKb+/O4Sk5B2Og==";
		String sessionKey = "rHYOUiSa6Sf6PxLEMMAMgw==";
		com.alibaba.fastjson.JSONObject obj = getUserInfo(data, sessionKey, iv);
		System.out.println(obj);
	}
    
    
    /**
     * 解密用户敏感数据获取用户信息
     * @param sessionKey 数据进行加密签名的密钥
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param iv 加密算法的初始向量
     * @return
     */
    public static com.alibaba.fastjson.JSONObject getUserInfo(String encryptedData,String sessionKey,String iv){
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        
        try {
               // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return com.alibaba.fastjson.JSONObject.parseObject(result);
                //return JSONObject.fromObject(result);
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException", e);
        } catch (NoSuchPaddingException e) {
            logger.error("NoSuchPaddingException", e);
        } catch (InvalidParameterSpecException e) {
            logger.error("InvalidParameterSpecException", e);
        } catch (IllegalBlockSizeException e) {
            logger.error("IllegalBlockSizeException", e);
        } catch (BadPaddingException e) {
            logger.error("BadPaddingException", e);
        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException", e);
        } catch (InvalidKeyException e) {
            logger.error("InvalidKeyException", e);
        } catch (InvalidAlgorithmParameterException e) {
            logger.error("InvalidAlgorithmParameterException", e);
        } catch (NoSuchProviderException e) {
            logger.error("NoSuchProviderException", e);
        } catch (Exception e)
        {
        	logger.error("decode weixin userinfo has error: ", e);
        }
        return null;
    }
    
}
