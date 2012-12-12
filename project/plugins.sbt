resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += Resolver.url(
  "sbt-plugin-releases",
  new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/")
)(Resolver.ivyStylePatterns)

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.1.0")

// https://github.com/sbt/sbt-onejar
addSbtPlugin("com.github.retronym" % "sbt-onejar" % "0.8")
