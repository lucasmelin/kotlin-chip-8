data class VM (
        val memory: ByteArray = ByteArray(4096), // RAM + ROM
        val stack: IntArray = IntArray(16), // Used to store return addresses
        val registers: ByteArray = ByteArray(16),
        var keys: ByteArray = ByteArray(16),
        var I: Int = 0, // 16-bit address register
        var pc: Int = 0, // Program counter
        var sc: Int = 0, // Stack counter
        var delay: Int = 0, // Delay timer
        var sound: Int = 0 // Sound timer
)