package com.zivkesten.simplecamera.permission


enum class PermissionState(val value: String) {
    INITIAL(Values.INITIAL),

    ALLOWED(Values.ALLOWED),

    DENIED(Values.DENIED);

    private object Values {
        const val INITIAL = "initial"
        const val ALLOWED = "allowed"
        const val DENIED = "denied"
    }
}
