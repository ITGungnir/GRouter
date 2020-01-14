package my.itgungnir.grouter.api

/**
 * Description:
 *
 * Created by ITGungnir on 2020-01-13
 */
class RouteLoader private constructor() {

    companion object {
        val instance by lazy { RouteLoader() }
    }

    fun getAdditionalRouteTableNames(): List<String> {
        val list = mutableListOf<String>()
        return list
    }
}
