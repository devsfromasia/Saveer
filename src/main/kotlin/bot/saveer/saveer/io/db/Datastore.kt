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

import com.mongodb.ReadPreference
import com.mongodb.client.ClientSession
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.DeleteOptions
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import org.bson.codecs.pojo.ClassModel

@Suppress("unused")
interface Datastore<TDocument> : MongoCollection<TDocument> {

    /**
     * The [ClassModel] used to map the documents.
     * @see ClassModel
     */
    val model: ClassModel<TDocument>


    /**
     * Updates the [document] in the database.
     * @see MongoCollection.updateOne
     */
    fun updateOne(document: TDocument): UpdateResult

    /**
     * Updates the [document] in the database on the [clientSession].
     * @see MongoCollection.updateOne
     */
    fun updateOne(clientSession: ClientSession, document: TDocument): UpdateResult

    /**
     * Updates the [document] in the database on the [clientSession] using the [updateOptions].
     * @see MongoCollection.updateOne
     */
    fun updateOne(clientSession: ClientSession, document: TDocument, updateOptions: UpdateOptions): UpdateResult

    /**
     * Updates the [document] in the database using the [updateOptions].
     * @see MongoCollection.updateOne
     */
    fun updateOne(document: TDocument, updateOptions: UpdateOptions): UpdateResult

    /**
     * Deletes the [document] from the database.
     * @see MongoCollection.deleteOne
     */
    fun deleteOne(document: TDocument): DeleteResult

    /**
     * Deletes the [document] from the database on the [clientSession].
     * @see MongoCollection.deleteOne
     */
    fun deleteOne(clientSession: ClientSession, document: TDocument): DeleteResult

    /**
     * Deletes the [document] from the database on the [clientSession] using the [deleteOptions].
     * @see MongoCollection.deleteOne
     */
    fun deleteOne(clientSession: ClientSession, document: TDocument, deleteOptions: DeleteOptions): DeleteResult

    /**
     * Deletes the [document] from the database using the [deleteOptions].
     * @see MongoCollection.deleteOne
     */
    fun deleteOne(document: TDocument, deleteOptions: DeleteOptions): DeleteResult

}