package io.applicative.datastore.util.reflection

import com.google.cloud.datastore.{Key => CloudKey}
import io.applicative.datastore.Key
import org.specs2.mutable.Specification
import org.specs2.mock.Mockito



class ReflectionHelperSpec extends Specification with Mockito {
  private val helper = new ReflectionHelper {}
  private val testInstance = TestClass()

  "ReflectionHelper" should {
    "extract correct class from type parameter" in {
      val clazz = helper.extractRuntimeClass[TestClass]()
      clazz shouldEqual classOf[TestClass]
    }

    "convert objects with supported fields to com.google.cloud.datastore.Entity" in {
      val key = Key(CloudKey.newBuilder("test", "TestClass", "test").build())
      val entity = helper.instanceToDatastoreEntity(key, testInstance, classOf[TestClass])
      entity.hasKey shouldEqual true
      entity.getNames.size() shouldEqual 13
      entity.getLong("byteVal") shouldEqual testInstance.byteVal
      entity.getLong("intVal") shouldEqual testInstance.intVal
      entity.getDouble("doubleVal") shouldEqual testInstance.doubleVal
      entity.getDouble("floatVal") shouldEqual testInstance.floatVal
      entity.getString("stringVal") shouldEqual testInstance.stringVal
      entity.getDateTime("googleDateTimeVal") shouldEqual testInstance.googleDateTimeVal
      entity.getBoolean("boolVal") shouldEqual testInstance.boolVal
    }

    "convert com.google.cloud.datastore.Entity to object type E" in {
      val key = Key(CloudKey.newBuilder("test", "TestClass", "test").setId(testInstance.id).build())
      val clazz = classOf[TestClass]
      val entity = helper.instanceToDatastoreEntity(key, testInstance, clazz)
      val res = helper.datastoreEntityToInstance[TestClass](entity, clazz)
      res shouldEqual testInstance
    }
  }

}
