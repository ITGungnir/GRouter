package my.itgungnir.grouter.api.interceptor

/**
 * 所有自定义拦截器应继承该类
 *
 * 传入一个Lambda表达式，当拦截器判定传递中止时回调该表达式
 */
abstract class BaseInterceptor(val errorCallback: () -> Unit) :
    Interceptor