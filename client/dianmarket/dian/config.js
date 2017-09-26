/**
 * 连接服务端地址
 */
const HOST = "http://127.0.0.1:8081";
//const HOST = "http://192.168.199.189:8081";
//const HOST = "https://www.2dian.com";

/**
 * 是否需要获取unionid
 * 若此项为true,则登录时候会多做一步服务端完全资料获取(包括获取unionid)
 */
const FULL_LOGIN = true;

module.exports = {
  host: HOST,
  fullLogin: FULL_LOGIN
}  