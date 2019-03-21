# Require minitest and location to run test
require 'minitest/autorun'
require_relative 'location'

# All tests for the Location Class
class LocationTest < Minitest::Test
  # UNIT TESTS FOR METHOD initialize(name, max rubies, max fake rubies)
  # Equivalence Classes:
  # (name, r, f) -> .name -> returns name
  # (name, r, f) -> .max_rubies -> returns r
  # (name, r, f) -> .max_fake_rubies -> returns f

  # When name is initialized, .name returns it
  def test_new_location_name
    loc = Location.new('NewLoc', 0, 0)
    assert_equal loc.name, 'NewLoc'
  end

  # When max_real_rubies is initialized, .max_rubies returns it
  def test_new_location_max_rubies
    loc = Location.new('NewLoc', 5, 0)
    assert_equal loc.max_rubies, 5
  end

  # When max_fake_rubies is initialized, .max_fake_rubies returns it
  def test_new_location_max_fake_rubies
    loc = Location.new('NewLoc', 0, 5)
    assert_equal loc.max_fake_rubies, 5
  end

  # UNIT TESTS FOR METHOD add_paths_to(loc, *locs)
  # Equivalence Classes:
  # (Location) -> .paths_to -> includes Location
  # (Location1, Location 2) -> .paths_to -> includes Location1, Location2

  # If 1 path is added, it is included in array paths_to
  def test_add_path_to
    loc = Location.new('NewLoc', 0, 0)
    loc2 = Location.new('NewLoc2', 0, 0)
    loc.add_paths_to(loc2)
    assert_includes loc.paths_to, loc2
  end

  # If 2 paths are added, the second is included in array paths_to
  def test_add_paths_to
    loc = Location.new('NewLoc', 0, 0)
    loc2 = Location.new('NewLoc2', 0, 0)
    loc3 = Location.new('NewLoc3', 0, 0)
    loc.add_paths_to(loc2, loc3)
    assert_includes loc.paths_to, loc3
  end

  # UNIT TEST FOR METHOD find_rubies(random number generator)
  # Equivalence Class:
  # (STUB_random_generator) -> .find_rubies -> 2 randomly generated ruby amounts within max

  # Test finding rubies by passing a Double random number generator
  # with a STUB .rand method
  def test_find_rubies
    loc = Location.new('NewLoc', 3, 3)
    # Make Double
    r_gen = Minitest::Mock.new('test_gen')
    # Add Stub Method
    def r_gen.rand(_range)
      1
    end
    assert_equal loc.find_rubies(r_gen), [1, 1]
  end

  # UNIT TEST FOR METHOD next_location(random number generator)
  # Equivalence Class:
  # (STUB_random_generator) -> .next_location -> a randomly chosen location within paths

  # Test finding the next location from the collection of paths
  # by creating a double random number with a STUB .rand method
  def test_next_location
    loc = Location.new('NewLoc', 0, 0)
    loc2 = Location.new('NewLoc2', 0, 0)
    loc3 = Location.new('NewLoc3', 0, 0)
    # Add paths
    loc.add_paths_to(loc2, loc3)
    # Make Double
    l_gen = Minitest::Mock.new('test_gen')
    # Add Stub Method
    def l_gen.rand(_range)
      1
    end
    assert_equal loc.next_location(l_gen), loc3
  end
end
