package androidx.navigation

import java.lang.StringBuilder
import java.util.AbstractCollection

fun NavController.debugPrintBackStack(): String {
    val sb = StringBuilder()
    val f = NavController::class.java.declaredFields.first { it.name == "mBackStack" }
    f.isAccessible = true
    val backStack = f.get(this) as AbstractCollection<NavBackStackEntry>
    backStack.forEach { entry ->
        sb.append(entry.destination.displayName).append("\n")
    }
    return sb.toString()
}