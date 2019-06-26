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

package bot.saveer.saveer.io.db.internal

import bot.saveer.saveer.io.db.Collection
import bot.saveer.saveer.io.db.Datastore
import com.mongodb.*
import com.mongodb.bulk.BulkWriteResult
import com.mongodb.client.*
import com.mongodb.client.model.*
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.*
import org.bson.conversions.Bson
import kotlin.reflect.full.findAnnotation

class DatastoreImpl<TDocument : Any>(
    database: MongoDatabase,
    _codecRegistry: CodecRegistry,
    pojoConventions: List<Convention>?,
    documentClass: Class<TDocument>
) : Datastore<TDocument> {

    private val internalCollection: MongoCollection<TDocument>
    private val idModel: PropertyModel<*>
    private val fieldMapping: Map<String, PropertyAccessor<out Any>>

    override val model: ClassModel<TDocument> = ClassModel.builder(documentClass).build()

    init {
        val collectionInfo = documentClass.kotlin.findAnnotation<Collection>()
            ?: throw IllegalStateException("Could not find @Collection annotation")
        val pojoCodecProvider = PojoCodecProvider
            .builder()
            .register(model)
        if (pojoConventions != null) {
            pojoCodecProvider.conventions(pojoConventions)
        }
        val codecRegistry = CodecRegistries.fromRegistries(
            _codecRegistry,
            CodecRegistries.fromProviders(pojoCodecProvider.build())
        )
        internalCollection = database.getCollection(
            collectionInfo.collection,
            documentClass
        ).withCodecRegistry(codecRegistry)
        idModel = model.idPropertyModel
        fieldMapping = model.propertyModels.map {
            it.readName to it.propertyAccessor
        }.toMap()
    }

    private fun equalsBson(document: TDocument) = Filters.eq(idModel.readName, idModel.propertyAccessor[document])

    private fun updateBson(document: TDocument) = Updates.combine(
        fieldMapping.asSequence()
            .map { (name, property) -> Updates.set(name, property[document]) }
            .toList()
    )

    override fun updateOne(document: TDocument) = updateOne(equalsBson(document), updateBson(document))

    override fun updateOne(clientSession: ClientSession, document: TDocument) =
        updateOne(clientSession, equalsBson(document), updateBson(document))

    override fun updateOne(
        clientSession: ClientSession,
        document: TDocument,
        updateOptions: UpdateOptions
    ) = updateOne(clientSession, equalsBson(document), updateBson(document), updateOptions)

    override fun updateOne(document: TDocument, updateOptions: UpdateOptions) =
        updateOne(equalsBson(document), updateBson(document), updateOptions)

    override fun updateOne(filter: Bson, update: Bson): UpdateResult = internalCollection.updateOne(filter, update)

    override fun updateOne(filter: Bson, update: Bson, updateOptions: UpdateOptions): UpdateResult =
        internalCollection.updateOne(filter, update, updateOptions)

    override fun updateOne(clientSession: ClientSession, filter: Bson, update: Bson): UpdateResult =
        internalCollection.updateOne(clientSession, filter, update)

    override fun updateOne(
        clientSession: ClientSession,
        filter: Bson,
        update: Bson,
        updateOptions: UpdateOptions
    ): UpdateResult = internalCollection.updateOne(clientSession, filter, update, updateOptions)

    override fun deleteOne(filter: Bson): DeleteResult = internalCollection.deleteOne(filter)

    override fun deleteOne(filter: Bson, options: DeleteOptions): DeleteResult =
        internalCollection.deleteOne(filter, options)

    override fun deleteOne(clientSession: ClientSession, filter: Bson): DeleteResult =
        internalCollection.deleteOne(clientSession, filter)

    override fun deleteOne(clientSession: ClientSession, filter: Bson, options: DeleteOptions): DeleteResult =
        internalCollection.deleteOne(clientSession, filter, options)

    override fun deleteOne(document: TDocument) = deleteOne(equalsBson(document))

    override fun deleteOne(clientSession: ClientSession, document: TDocument) =
        deleteOne(clientSession, equalsBson(document))

    override fun deleteOne(
        clientSession: ClientSession,
        document: TDocument,
        deleteOptions: DeleteOptions
    ) = deleteOne(clientSession, equalsBson(document), deleteOptions)

    override fun deleteOne(document: TDocument, deleteOptions: DeleteOptions) =
        deleteOne(equalsBson(document), deleteOptions)

    override fun findOneAndReplace(filter: Bson, replacement: TDocument): TDocument? =
        internalCollection.findOneAndReplace(filter, replacement)

    override fun findOneAndReplace(
        filter: Bson,
        replacement: TDocument,
        options: FindOneAndReplaceOptions
    ): TDocument? = internalCollection.findOneAndReplace(filter, replacement, options)

    override fun findOneAndReplace(clientSession: ClientSession, filter: Bson, replacement: TDocument): TDocument? =
        internalCollection.findOneAndReplace(clientSession, filter, replacement)

    override fun findOneAndReplace(
        clientSession: ClientSession,
        filter: Bson,
        replacement: TDocument,
        options: FindOneAndReplaceOptions
    ): TDocument? = internalCollection.findOneAndReplace(clientSession, filter, replacement, options)

    override fun drop() = internalCollection.drop()

    override fun drop(clientSession: ClientSession) = internalCollection.drop(clientSession)

    override fun renameCollection(newCollectionNamespace: MongoNamespace) = throw UnsupportedOperationException()

    override fun renameCollection(
        newCollectionNamespace: MongoNamespace,
        renameCollectionOptions: RenameCollectionOptions
    ) = throw UnsupportedOperationException()

    override fun renameCollection(clientSession: ClientSession, newCollectionNamespace: MongoNamespace) =
        throw UnsupportedOperationException()

    override fun renameCollection(
        clientSession: ClientSession,
        newCollectionNamespace: MongoNamespace,
        renameCollectionOptions: RenameCollectionOptions
    ) = throw UnsupportedOperationException()

    override fun deleteMany(filter: Bson): DeleteResult = internalCollection.deleteMany(filter)

    override fun deleteMany(filter: Bson, options: DeleteOptions): DeleteResult =
        internalCollection.deleteMany(filter, options)

    override fun deleteMany(clientSession: ClientSession, filter: Bson): DeleteResult =
        internalCollection.deleteMany(clientSession, filter)

    override fun deleteMany(clientSession: ClientSession, filter: Bson, options: DeleteOptions): DeleteResult =
        internalCollection.deleteMany(clientSession, filter, options)

    override fun watch(): ChangeStreamIterable<TDocument> = internalCollection.watch()

    override fun <TResult : Any?> watch(resultClass: Class<TResult>): ChangeStreamIterable<TResult> =
        internalCollection.watch(resultClass)

    override fun watch(pipeline: MutableList<out Bson>): ChangeStreamIterable<TDocument> =
        internalCollection.watch(pipeline)

    override fun <TResult : Any?> watch(
        pipeline: MutableList<out Bson>,
        resultClass: Class<TResult>
    ): ChangeStreamIterable<TResult> = internalCollection.watch(pipeline, resultClass)

    override fun watch(clientSession: ClientSession): ChangeStreamIterable<TDocument> =
        internalCollection.watch(clientSession)

    override fun <TResult : Any?> watch(
        clientSession: ClientSession,
        resultClass: Class<TResult>
    ): ChangeStreamIterable<TResult> = internalCollection.watch(clientSession, resultClass)

    override fun watch(
        clientSession: ClientSession,
        pipeline: MutableList<out Bson>
    ): ChangeStreamIterable<TDocument> = internalCollection.watch(clientSession, pipeline)

    override fun <TResult : Any?> watch(
        clientSession: ClientSession,
        pipeline: MutableList<out Bson>,
        resultClass: Class<TResult>
    ): ChangeStreamIterable<TResult> = internalCollection.watch(clientSession, pipeline, resultClass)

    override fun aggregate(pipeline: MutableList<out Bson>): AggregateIterable<TDocument> =
        internalCollection.aggregate(pipeline)

    override fun <TResult : Any?> aggregate(
        pipeline: MutableList<out Bson>,
        resultClass: Class<TResult>
    ): AggregateIterable<TResult> = internalCollection.aggregate(pipeline, resultClass)

    override fun aggregate(
        clientSession: ClientSession,
        pipeline: MutableList<out Bson>
    ): AggregateIterable<TDocument> = internalCollection.aggregate(clientSession, pipeline)

    override fun <TResult : Any?> aggregate(
        clientSession: ClientSession,
        pipeline: MutableList<out Bson>,
        resultClass: Class<TResult>
    ): AggregateIterable<TResult> = internalCollection.aggregate(clientSession, pipeline, resultClass)

    override fun replaceOne(filter: Bson, replacement: TDocument): UpdateResult =
        internalCollection.replaceOne(filter, replacement)

    override fun replaceOne(filter: Bson, replacement: TDocument, updateOptions: UpdateOptions): UpdateResult =
        throw UnsupportedOperationException()

    override fun replaceOne(filter: Bson, replacement: TDocument, replaceOptions: ReplaceOptions): UpdateResult =
        internalCollection.replaceOne(filter, replacement, replaceOptions)

    override fun replaceOne(clientSession: ClientSession, filter: Bson, replacement: TDocument): UpdateResult =
        internalCollection.replaceOne(clientSession, filter, replacement)

    override fun replaceOne(
        clientSession: ClientSession,
        filter: Bson,
        replacement: TDocument,
        updateOptions: UpdateOptions
    ): UpdateResult = throw UnsupportedOperationException()

    override fun replaceOne(
        clientSession: ClientSession,
        filter: Bson,
        replacement: TDocument,
        replaceOptions: ReplaceOptions
    ): UpdateResult = internalCollection.replaceOne(clientSession, filter, replacement, replaceOptions)

    override fun count() = throw UnsupportedOperationException()

    override fun count(filter: Bson) = throw UnsupportedOperationException()

    override fun count(filter: Bson, options: CountOptions) = throw UnsupportedOperationException()

    override fun count(clientSession: ClientSession) = throw UnsupportedOperationException()

    override fun count(clientSession: ClientSession, filter: Bson) = throw UnsupportedOperationException()

    override fun count(clientSession: ClientSession, filter: Bson, options: CountOptions) =
        throw UnsupportedOperationException()

    override fun createIndex(keys: Bson): String = internalCollection.createIndex(keys)

    override fun createIndex(keys: Bson, indexOptions: IndexOptions): String =
        internalCollection.createIndex(keys, indexOptions)

    override fun createIndex(clientSession: ClientSession, keys: Bson): String =
        internalCollection.createIndex(clientSession, keys)

    override fun createIndex(clientSession: ClientSession, keys: Bson, indexOptions: IndexOptions): String =
        internalCollection.createIndex(clientSession, keys, indexOptions)

    override fun withWriteConcern(writeConcern: WriteConcern): MongoCollection<TDocument> =
        throw UnsupportedOperationException()

    override fun mapReduce(mapFunction: String, reduceFunction: String): MapReduceIterable<TDocument> =
        internalCollection.mapReduce(mapFunction, reduceFunction)

    override fun <TResult : Any?> mapReduce(
        mapFunction: String,
        reduceFunction: String,
        resultClass: Class<TResult>
    ): MapReduceIterable<TResult> = internalCollection.mapReduce(mapFunction, reduceFunction, resultClass)

    override fun mapReduce(
        clientSession: ClientSession,
        mapFunction: String,
        reduceFunction: String
    ): MapReduceIterable<TDocument> = internalCollection.mapReduce(clientSession, mapFunction, reduceFunction)

    override fun <TResult : Any?> mapReduce(
        clientSession: ClientSession,
        mapFunction: String,
        reduceFunction: String,
        resultClass: Class<TResult>
    ): MapReduceIterable<TResult> =
        internalCollection.mapReduce(clientSession, mapFunction, reduceFunction, resultClass)

    override fun insertMany(documents: MutableList<out TDocument>) = internalCollection.insertMany(documents)

    override fun insertMany(documents: MutableList<out TDocument>, options: InsertManyOptions) =
        internalCollection.insertMany(documents, options)

    override fun insertMany(clientSession: ClientSession, documents: MutableList<out TDocument>) =
        internalCollection.insertMany(clientSession, documents)

    override fun insertMany(
        clientSession: ClientSession,
        documents: MutableList<out TDocument>,
        options: InsertManyOptions
    ) = internalCollection.insertMany(clientSession, documents, options)

    override fun dropIndexes() = internalCollection.dropIndexes()

    override fun dropIndexes(clientSession: ClientSession) = internalCollection.dropIndexes(clientSession)

    override fun dropIndexes(dropIndexOptions: DropIndexOptions) = internalCollection.dropIndexes(dropIndexOptions)

    override fun dropIndexes(clientSession: ClientSession, dropIndexOptions: DropIndexOptions) =
        internalCollection.dropIndexes(clientSession, dropIndexOptions)

    override fun <NewTDocument : Any?> withDocumentClass(clazz: Class<NewTDocument>): MongoCollection<NewTDocument> =
        throw UnsupportedOperationException()

    override fun findOneAndUpdate(filter: Bson, update: Bson): TDocument? =
        internalCollection.findOneAndUpdate(filter, update)

    override fun findOneAndUpdate(filter: Bson, update: Bson, options: FindOneAndUpdateOptions): TDocument? =
        internalCollection.findOneAndUpdate(filter, update, options)

    override fun findOneAndUpdate(clientSession: ClientSession, filter: Bson, update: Bson): TDocument? =
        internalCollection.findOneAndUpdate(clientSession, filter, update)

    override fun findOneAndUpdate(
        clientSession: ClientSession,
        filter: Bson,
        update: Bson,
        options: FindOneAndUpdateOptions
    ): TDocument? = internalCollection.findOneAndUpdate(clientSession, filter, update, options)

    override fun withCodecRegistry(codecRegistry: CodecRegistry): MongoCollection<TDocument> =
        throw UnsupportedOperationException()

    override fun insertOne(document: TDocument) = internalCollection.insertOne(document)

    override fun insertOne(document: TDocument, options: InsertOneOptions) =
        internalCollection.insertOne(document, options)

    override fun insertOne(clientSession: ClientSession, document: TDocument) =
        internalCollection.insertOne(clientSession, document)

    override fun insertOne(clientSession: ClientSession, document: TDocument, options: InsertOneOptions) =
        internalCollection.insertOne(clientSession, document, options)

    override fun getDocumentClass(): Class<TDocument> = internalCollection.documentClass

    override fun find(): FindIterable<TDocument> = internalCollection.find()

    override fun <TResult : Any?> find(resultClass: Class<TResult>): FindIterable<TResult> =
        internalCollection.find(resultClass)

    override fun find(filter: Bson): FindIterable<TDocument> = internalCollection.find(filter)

    override fun <TResult : Any?> find(filter: Bson, resultClass: Class<TResult>): FindIterable<TResult> =
        internalCollection.find(filter, resultClass)

    override fun find(clientSession: ClientSession): FindIterable<TDocument> = internalCollection.find(clientSession)

    override fun <TResult : Any?> find(
        clientSession: ClientSession,
        resultClass: Class<TResult>
    ): FindIterable<TResult> = internalCollection.find(clientSession, resultClass)

    override fun find(clientSession: ClientSession, filter: Bson): FindIterable<TDocument> =
        internalCollection.find(clientSession, filter)

    override fun <TResult : Any?> find(
        clientSession: ClientSession,
        filter: Bson,
        resultClass: Class<TResult>
    ): FindIterable<TResult> = internalCollection.find(clientSession, filter, resultClass)

    override fun dropIndex(indexName: String) = internalCollection.dropIndex(indexName)

    override fun dropIndex(indexName: String, dropIndexOptions: DropIndexOptions) =
        internalCollection.dropIndex(indexName, dropIndexOptions)

    override fun dropIndex(keys: Bson) = internalCollection.dropIndex(keys)

    override fun dropIndex(keys: Bson, dropIndexOptions: DropIndexOptions) =
        internalCollection.dropIndex(keys, dropIndexOptions)

    override fun dropIndex(clientSession: ClientSession, indexName: String) =
        internalCollection.dropIndex(clientSession, indexName)

    override fun dropIndex(clientSession: ClientSession, keys: Bson) =
        internalCollection.dropIndex(clientSession, keys)

    override fun dropIndex(clientSession: ClientSession, indexName: String, dropIndexOptions: DropIndexOptions) =
        internalCollection.dropIndex(clientSession, indexName, dropIndexOptions)

    override fun dropIndex(clientSession: ClientSession, keys: Bson, dropIndexOptions: DropIndexOptions) =
        internalCollection.dropIndex(clientSession, keys, dropIndexOptions)

    override fun countDocuments() = internalCollection.countDocuments()

    override fun countDocuments(filter: Bson) = internalCollection.countDocuments(filter)

    override fun countDocuments(filter: Bson, options: CountOptions) =
        internalCollection.countDocuments(filter, options)

    override fun countDocuments(clientSession: ClientSession) = internalCollection.countDocuments(clientSession)

    override fun countDocuments(clientSession: ClientSession, filter: Bson) =
        internalCollection.countDocuments(clientSession, filter)

    override fun countDocuments(clientSession: ClientSession, filter: Bson, options: CountOptions) =
        internalCollection.countDocuments(clientSession, filter, options)

    override fun getCodecRegistry(): CodecRegistry = internalCollection.codecRegistry

    override fun withReadConcern(readConcern: ReadConcern) = throw UnsupportedOperationException()

    override fun createIndexes(indexes: MutableList<IndexModel>): MutableList<String> =
        internalCollection.createIndexes(indexes)

    override fun createIndexes(
        indexes: MutableList<IndexModel>,
        createIndexOptions: CreateIndexOptions
    ): MutableList<String> = internalCollection.createIndexes(indexes, createIndexOptions)

    override fun createIndexes(clientSession: ClientSession, indexes: MutableList<IndexModel>): MutableList<String> =
        internalCollection.createIndexes(clientSession, indexes)

    override fun createIndexes(
        clientSession: ClientSession,
        indexes: MutableList<IndexModel>,
        createIndexOptions: CreateIndexOptions
    ): MutableList<String> = internalCollection.createIndexes(clientSession, indexes, createIndexOptions)

    override fun listIndexes(): ListIndexesIterable<Document> = internalCollection.listIndexes()

    override fun <TResult : Any?> listIndexes(resultClass: Class<TResult>): ListIndexesIterable<TResult> =
        internalCollection.listIndexes(resultClass)

    override fun listIndexes(clientSession: ClientSession): ListIndexesIterable<Document> =
        internalCollection.listIndexes()

    override fun <TResult : Any?> listIndexes(
        clientSession: ClientSession,
        resultClass: Class<TResult>
    ): ListIndexesIterable<TResult> = internalCollection.listIndexes(clientSession, resultClass)

    override fun withReadPreference(readPreference: ReadPreference) = throw UnsupportedOperationException()

    override fun getNamespace(): MongoNamespace = internalCollection.namespace

    override fun updateMany(filter: Bson, update: Bson): UpdateResult = internalCollection.updateMany(filter, update)

    override fun updateMany(filter: Bson, update: Bson, updateOptions: UpdateOptions): UpdateResult =
        internalCollection.updateMany(filter, update, updateOptions)

    override fun updateMany(clientSession: ClientSession, filter: Bson, update: Bson): UpdateResult =
        internalCollection.updateMany(clientSession, filter, update)

    override fun updateMany(
        clientSession: ClientSession,
        filter: Bson,
        update: Bson,
        updateOptions: UpdateOptions
    ): UpdateResult = internalCollection.updateMany(clientSession, filter, update, updateOptions)

    override fun <TResult : Any?> distinct(
        fieldName: String,
        resultClass: Class<TResult>
    ): DistinctIterable<TResult> = internalCollection.distinct(fieldName, resultClass)

    override fun <TResult : Any?> distinct(
        fieldName: String,
        filter: Bson,
        resultClass: Class<TResult>
    ): DistinctIterable<TResult> = internalCollection.distinct(fieldName, filter, resultClass)

    override fun <TResult : Any?> distinct(
        clientSession: ClientSession,
        fieldName: String,
        resultClass: Class<TResult>
    ): DistinctIterable<TResult> = internalCollection.distinct(clientSession, fieldName, resultClass)

    override fun <TResult : Any?> distinct(
        clientSession: ClientSession,
        fieldName: String,
        filter: Bson,
        resultClass: Class<TResult>
    ): DistinctIterable<TResult> = internalCollection.distinct(clientSession, fieldName, resultClass)

    override fun bulkWrite(requests: MutableList<out WriteModel<out TDocument>>): BulkWriteResult =
        internalCollection.bulkWrite(requests)

    override fun bulkWrite(
        requests: MutableList<out WriteModel<out TDocument>>,
        options: BulkWriteOptions
    ): BulkWriteResult = internalCollection.bulkWrite(requests, options)

    override fun bulkWrite(
        clientSession: ClientSession,
        requests: MutableList<out WriteModel<out TDocument>>
    ): BulkWriteResult = internalCollection.bulkWrite(clientSession, requests)

    override fun bulkWrite(
        clientSession: ClientSession,
        requests: MutableList<out WriteModel<out TDocument>>,
        options: BulkWriteOptions
    ): BulkWriteResult = internalCollection.bulkWrite(clientSession, requests, options)

    override fun findOneAndDelete(filter: Bson): TDocument? = internalCollection.findOneAndDelete(filter)

    override fun findOneAndDelete(filter: Bson, options: FindOneAndDeleteOptions): TDocument? =
        internalCollection.findOneAndDelete(filter, options)

    override fun findOneAndDelete(clientSession: ClientSession, filter: Bson): TDocument? =
        internalCollection.findOneAndDelete(clientSession, filter)

    override fun findOneAndDelete(
        clientSession: ClientSession,
        filter: Bson,
        options: FindOneAndDeleteOptions
    ): TDocument? = internalCollection.findOneAndDelete(clientSession, filter, options)

    override fun getWriteConcern(): WriteConcern = internalCollection.writeConcern

    override fun estimatedDocumentCount() = internalCollection.estimatedDocumentCount()

    override fun estimatedDocumentCount(options: EstimatedDocumentCountOptions) =
        internalCollection.estimatedDocumentCount(options)

    override fun getReadConcern(): ReadConcern = internalCollection.readConcern

    override fun getReadPreference(): ReadPreference = internalCollection.readPreference
}