package registrar

import caliban.GraphQL.graphQL
import caliban.RootResolver
import registrar.models.{Fail, User, UserId}
import registrar.userRepo.{UserRepo, accessUserRepoService, inMemUserRepo}
import zio._
import zio.console.{Console, putStrLn}

object userGqlApi {

  final case class GetUserArgs(userId: UserId)

  final case class Queries(
                          readUser: GetUserArgs => IO[Fail, User]
                          )
  def queries(urs: userRepo.Service): Queries = Queries(gua => urs.readUser(gua.userId))

}
