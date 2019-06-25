/*
 * Saveer
 * Copyright (C) 2019  Yannick Seeger, Leon Kappes, Michael Rittmeister
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package bot.saveer.saveer.command

import java.util.*

@Suppress("unused")
abstract class CommandArguments: Collection<String> {

    abstract val list: List<String>

    abstract val array: Array<String>

    override val size: Int
        get() = list.size

    @JvmOverloads
    fun list(from: Int = 0, to: Int = size) = list.subList(from, to)

    @JvmOverloads
    fun array(from: Int = 0, to: Int = size): Array<String> = Arrays.copyOfRange(array, from, to)

    @JvmOverloads
    fun string(separator: CharSequence = ", ", prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...") = list.joinToString(separator, prefix, postfix, limit, truncated)

    @JvmOverloads
    fun stringInRange(from: Int = 0, to: Int = size, separator: CharSequence = ", ", prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...") = list(from, to).joinToString(separator, prefix, postfix, limit, truncated)

    operator fun get(index: Int) = list[index]

    override fun isEmpty() = list.isEmpty()

    override operator fun contains(element: String) = list.contains(element)

    override operator fun iterator() = list.iterator()

    override fun containsAll(elements: Collection<String>) = throw UnsupportedOperationException("Command arguments do not support contains all")

}
