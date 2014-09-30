// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository 
resolvers += Resolver.typesafeRepo("releases")

// SBT project definition deprecations
scalacOptions := Seq("-feature", "-deprecation")
