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

package bot.saveer.saveer.io.config

import io.github.cdimascio.dotenv.Dotenv

class EnvConfig : Config {

    override val token: String
    override val prefix: String
    override val owners: List<String>
    override val dbHost: String
    override val dbPort: Int
    override val dbName: String
    override val dbUser: String
    override val dbPassword: String

    init {
        val dotenv = Dotenv.load()
        token = dotenv["SAVEER_TOKEN"] ?: "xxx"
        prefix = dotenv["SAVEER_PREFIX"] ?: "s!"
        owners = (dotenv["SAVEER_OWNERS"] ?: "").split(";")

        // Database
        dbHost = dotenv["SAVEER_DB_HOST"] ?: "localhost"
        dbPort = (dotenv["SAVEER_DB_PORT"] ?: "27017").toInt()
        dbName = dotenv["SAVEER_DB_NAME"] ?: "saveer"
        dbUser = dotenv["SAVEER_DB_USER"] ?: "myUser"
        dbPassword = dotenv["SAVEER_DB_PASSWORD"] ?: "myPassword"
    }
}