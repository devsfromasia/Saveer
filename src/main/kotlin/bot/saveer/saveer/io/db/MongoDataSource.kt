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

package bot.saveer.saveer.io.db

import bot.saveer.saveer.core.Saveer
import bot.saveer.saveer.io.config.Config
import bot.saveer.saveer.io.db.internal.DatastoreImpl
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.Convention
import kotlin.reflect.KClass

/**
 * Represents a MongoDB datasource
 */
class MongoDataSource private constructor(
    username: String,
    password: String,
    loginDatabase: CharArray,
    clusterHosts: List<ServerAddress>,
    database: String
) {

    /**
     * Creates a new MongoDB datasource.
     * @param bot the [Saveer] instance.
     */
    constructor(bot: Saveer) : this(bot.config)

    private constructor(config: Config) : this(
        config.dbUser,
        config.dbPassword,
        config.dbAuthenticationDatabase.toCharArray(),
        config.dbHosts.map { ServerAddress(it) },
        config.dbName
    )

    val client: MongoClient = MongoClients.create(
        MongoClientSettings.builder()
            .applyToClusterSettings {
                it.hosts(clusterHosts)
            }
            .credential(
                MongoCredential.createCredential(
                    username, password, loginDatabase
                )
            )
            .build()
    )

    val database: MongoDatabase = client.getDatabase(database)

    fun <TDocument : Any> getCollection(
        database: MongoDatabase,
        codecRegistry: CodecRegistry = MongoClientSettings.getDefaultCodecRegistry(),
        conventions: List<Convention>,
        documentClass: Class<TDocument>
    ): Datastore<TDocument> {
        return DatastoreImpl(
            database,
            codecRegistry,
            conventions,
            documentClass
        )
    }

}