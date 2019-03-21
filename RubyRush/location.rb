# Location Class, for storing information on each map location
class Location
  # Constructor for each location, defining name,
  # max rubies, and max fake rubies
  def initialize(name, max_rubies, max_fake_rubies)
    @name = name
    @max_rubies = max_rubies
    @max_fake_rubies = max_fake_rubies
    @paths_to = []
  end

  # Attribute Reader, for quick access to instance variables
  attr_reader :name, :max_rubies, :max_fake_rubies, :paths_to

  # Add a Path, or multiple paths, from this location to another
  def add_paths_to(first_location, *other_locations)
    @paths_to.push(first_location)
    other_locations.each { |loc| @paths_to.push(loc) }
  end

  # Given a seeded random generator, return an array containing
  # two random ruby amounts
  def find_rubies(ruby_gen)
    [ruby_gen.rand(0..@max_rubies), ruby_gen.rand(0..@max_fake_rubies)]
  end

  # Given a seeded random generator, return a next location on the path
  def next_location(loc_gen)
    loc_index = loc_gen.rand(0...@paths_to.size)
    @paths_to[loc_index]
  end
end
