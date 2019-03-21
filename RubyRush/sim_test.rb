# Require minitest and Sim to run test
require 'minitest/autorun'
require_relative 'sim'
require_relative 'rubyist'
require_relative 'location'

# All tests for the Sim Class
class SimTest < Minitest::Test
  # UNIT TESTS FOR METHOD initialize(rubyist, turns, random_generator)
  # Equivalence Classes:
  # (rubyist, turns, random_generator) -> .rubyist -> returns current Rubyist
  # (rubyist, turns, random_generator) -> .turns -> returns turn amount
  # (rubyist, turns, random_generator) -> .rndm -> returns the random generator
  # (rubyist, turns, random_generator) -> .current_location -> returns Enumerable Canyon

  # If initialized, calling .rubyist will return the current rubyist
  def test_initialize_rubyist
    rubyist = Minitest::Mock.new('rubyist')
    simulator = Sim.new(rubyist, nil, nil)
    assert_equal simulator.rubyist, rubyist
  end

  # If initialized, calling .turns will return the current turns
  def test_initialize_turns
    turns = Minitest::Mock.new('turns')
    simulator = Sim.new(nil, turns, nil)
    assert_equal simulator.turns, turns
  end

  # If initialized, calling .rndm will return the current random generator
  def test_initialize_random
    random = Minitest::Mock.new('random')
    simulator = Sim.new(nil, nil, random)
    assert_equal simulator.rndm, random
  end

  # If initialized, calling .current_location will return a location with the name Enumerable Canyon
  def test_initialize_current_location
    simulator = Sim.new(nil, nil, nil)
    assert_equal simulator.current_location.name, 'Enumerable Canyon'
  end

  # UNIT TESTS FOR METHOD init_paths(locations)
  # Equivalence Classes:
  # (locations) = HashMap of correctly initialized locations -> correct paths_to for all locations
  # (locations) = Incorrectly initialized locations -> raises StandardError with msg

  # If correctly initialized, will add correct paths to and from all Locations
  def test_init_paths_no_error
    locations = {}
    locations['Enumerable Canyon'] = Location.new('Enumerable Canyon', 1, 1)
    locations['Duck Type Beach'] = Location.new('Duck Type Beach', 2, 2)
    locations['Monkey Patch City'] = Location.new('Monkey Patch City', 1, 1)
    locations['Nil Town'] = Location.new('Nil Town', 0, 3)
    locations['Matzburg'] = Location.new('Matzburg', 3, 0)
    locations['Hash Crossing'] = Location.new('Hash Crossing', 2, 2)
    locations['Dynamic Palisades'] = Location.new('Dynamic Palisades', 2, 2)

    simulator = Sim.new(nil, nil, nil)
    simulator.init_paths(locations)
    assert_equal simulator.current_location.paths_to[0].name, locations['Duck Type Beach'].name
  end

  # If incorrectly initialized, will raise a standarderror with a corresponding message
  # EDGE CASE
  def test_init_paths_error
    locations = {}
    simulator = Sim.new(nil, nil, nil)
    assert_raises StandardError do
      simulator.init_path(locations)
    end
  end

  # UNIT TESTS FOR METHOD ruby_format_print(rubies)
  # Equivalence Classes:
  # rubies = [2, 2] -> returns "Found 2 rubies and 2 fake rubies in Enumerable Canyon."
  # rubies = [1, 0] -> returns "Found 1 ruby in Enumerable Canyon."
  # rubies = [0, 2] -> returns "Found 2 fake rubies in Enumerable Canyon."

  # If 2 real and 2 fake rubies are passed in, print the correct formatting
  def test_format_print_two_plural
    simulator = Sim.new(nil, nil, nil)
    assert_equal simulator.ruby_format_print([2, 2]), "\tFound 2 rubies and 2 fake rubies in Enumerable Canyon.\n"
  end

  # If 1 real and no fake rubies are passed in, print the correct formatting
  def test_format_print_one_real
    simulator = Sim.new(nil, nil, nil)
    assert_equal simulator.ruby_format_print([1, 0]), "\tFound 1 ruby in Enumerable Canyon.\n"
  end

  # If no real and 2 fake rubies are passed in, print the correct formatting
  def test_format_print_two_fake
    simulator = Sim.new(nil, nil, nil)
    assert_equal simulator.ruby_format_print([0, 2]), "\tFound 2 fake rubies in Enumerable Canyon.\n"
  end

  # UNIT TEST FOR METHOD run
  # Equivalence Class:
  # 1 turn, 1 stub location, initialized rubyist -> return 'Going home empty-handed.'

  def test_run
    prospector = Rubyist.new(1)
    turns = 1
    random_generator = Minitest::Mock.new('rndm')
    def random_generator.rand(*_num)
      0
    end
    simulator = Sim.new(prospector, turns, random_generator)
    assert_equal simulator.run, 'Going home empty-handed.'
  end
end
