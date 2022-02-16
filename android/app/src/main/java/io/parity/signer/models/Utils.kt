package io.parity.signer.models

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import org.json.JSONArray
import org.json.JSONObject

/**
 * Decodes from hex string into number array
 */
fun String.decodeHex(): ByteArray {
	return chunked(2).map { it.toInt(16).toByte() }.toByteArray()
}

/**
 * Replace middle of long string with "..."
 * length: number of symbols to keep on either side
 * if message is too short, does not modify it
 */
fun String.abbreviateString(length: Int): String {
	return if (this.length > length*2) {
		this.substring(0, length) + "..." + this.substring(this.length - length, this.length)
	} else {
		this
	}
}

/**
 * Encodes number array into string
 */
fun ByteArray.encodeHex(): String {
	return this.joinToString(separator = "") { byte -> "%02x".format(byte) }
}

/**
 * Concatenator for JSON arrays
 * TODO: should abomination not be needed in final version
 */
fun concatJSONArray(vararg arrays: JSONArray): JSONArray {
	val result = JSONArray()
	for (array in arrays) {
		for (i in 0 until array.length()) {
			result.put(array.getJSONObject(i))
		}
	}
	return result
}

/**
 * Sorter for transaction cards
 */
fun sortCards(array: JSONArray): JSONArray {
	val sortable = emptyList<JSONObject>().toMutableList()
	for(i in 0 until array.length()) {
		sortable += array.getJSONObject(i)
	}
	return JSONArray(sortable.sortedBy { item -> item.getInt("index") } )
}

/**
 * Specialized tool to decode png images generated by rust code
 */
fun String.intoImageBitmap(): ImageBitmap {
	val picture = this.decodeHex()
	return try {
		BitmapFactory.decodeByteArray(picture, 0, picture.size).asImageBitmap()
	} catch (e: java.lang.Exception) {
		Log.d("image decoding error", e.toString())
		ImageBitmap(1, 1)
	}
}

fun JSONArray.toListOfStrings(): List<String> {
	val output = emptyList<String>().toMutableList()
	for(i in 0 until this.length()) {
		output += this.getString(i)
	}
	return output
}

fun JSONArray.toListOfJSONObjects(): List<JSONObject> {
	val output = emptyList<JSONObject>().toMutableList()
	for(i in 0 until this.length()) {
		output += this.getJSONObject(i)
	}
	return output
}
