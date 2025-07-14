package com.yapp.breake.core.util

/**
 * Validates if the input string on Text Field is a valid value.
 *
 * The Following conditions must be met:
 *
 * 1. A valid value must be between 2 and 10 characters long
 *
 * 2. A valid value must not contain spaces
 *
 * 3. A valid value must consist only of letters and digits
 *
 * @return true if the string is a valid value, false otherwise.
 */
fun String.isValidInput(): Boolean = this.length in 2..10 &&
	!this.contains(" ") &&
	this.all { it.isLetterOrDigit() }
