package com.teambrake.brake.core.model.app

data class App(
	val packageName: String,
	val id: Long?,
	val name: String,
	val icon: ByteArray?,
	val category: String,
) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as App

		if (packageName != other.packageName) return false
		if (name != other.name) return false
		if (icon != null) {
			if (other.icon == null) return false
			if (!icon.contentEquals(other.icon)) return false
		} else if (other.icon != null) return false
		if (category != other.category) return false

		return true
	}

	override fun hashCode(): Int {
		var result = packageName.hashCode()
		result = 31 * result + name.hashCode()
		result = 31 * result + (icon?.contentHashCode() ?: 0)
		result = 31 * result + category.hashCode()
		return result
	}
}
