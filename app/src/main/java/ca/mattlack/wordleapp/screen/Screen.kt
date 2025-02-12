package ca.mattlack.wordleapp.screen

sealed class Screen(val route: String) {
    data object Days : Screen("days")
    data object Play : Screen("play")

    fun withArgs(vararg args: Pair<String, Any>): String {
        return route + "?" + args.joinToString("&") { "${it.first}=${it.second}" }
    }
}