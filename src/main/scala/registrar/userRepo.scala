package registrar

import registrar.models.{Fail, User, UserId}
import zio.{Has, IO, UIO, ULayer, URIO, ZIO, ZLayer}

object userRepo {

  type UserRepo = Has[Service]
  trait Service {
    def createUser(u: User): IO[Fail, Unit]
    def readUser(ui: UserId): IO[Fail, User]
    def updateUser(ui: UserId, u: User): IO[Fail, Unit]
    def deleteUser(ui: UserId): IO[Fail, Unit]
  }

  val accessUserRepoService: URIO[Has[Service], Service] = ZIO.service[Service]

  val inMemUserRepo: ULayer[Has[Service]] =
    ZLayer.succeed(
      new Service {
        val inMemDb = scala.collection.mutable.Map.empty[UserId, User]

        override def createUser(u: User): IO[Fail, Unit] = {
          if(inMemDb.contains(u.userId)) IO.fail(Fail("User Already Exists!")) else UIO(inMemDb.addOne(u.userId -> u))
        }

        override def readUser(ui: UserId): IO[Fail, User] = {
          ZIO.fromOption(inMemDb.get(ui)).orElseFail(Fail("User Doesn't Exist!"))
        }

        override def updateUser(ui: UserId, u: User): IO[Fail, Unit] = {
          if(inMemDb.contains(ui)) UIO(inMemDb.update(ui,u)) else IO.fail(Fail("User Doesn't Exist!"))
        }

        override def deleteUser(ui: UserId): IO[Fail, Unit] = {
          if(inMemDb.contains(ui)) UIO(inMemDb.subtractOne(ui)) else IO.fail(Fail("User Doesn't Exist!"))
        }

      }
    )

}
