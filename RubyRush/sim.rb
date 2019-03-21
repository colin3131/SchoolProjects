# Require Rubyist and location classes
require_relative 'rubyist'
require_relative 'location'

# Simulator for one rubyist Class
class Sim
  def initialize(rubyist, turns, random_generator)
    @rubyist = rubyist
    @current_location = init_locations
    @turns = turns
    @rndm = random_generator
  end

  attr_reader :rubyist, :current_location, :turns, :rndm

  def init_locations
    # Initialize all Locations in a Hash Map
    locations = {}
    locations['Enumerable Canyon'] = Location.new('Enumerable Canyon', 1, 1)
    locations['Duck Type Beach'] = Location.new('Duck Type Beach', 2, 2)
    locations['Monkey Patch City'] = Location.new('Monkey Patch City', 1, 1)
    locations['Nil Town'] = Location.new('Nil Town', 0, 3)
    locations['Matzburg'] = Location.new('Matzburg', 3, 0)
    locations['Hash Crossing'] = Location.new('Hash Crossing', 2, 2)
    locations['Dynamic Palisades'] = Location.new('Dynamic Palisades', 2, 2)

    # Initialize Paths of Locations
    init_paths(locations)

    # Return first city, enumerable_canyon
    locations['Enumerable Canyon']
  end

  # Initialize all Locations' paths
  # (ABC size is pretty big, but can't get around it)
  def init_paths(locations)
    locations['Enumerable Canyon'].add_paths_to(locations['Duck Type Beach'],
                                                locations['Monkey Patch City'])
    locations['Duck Type Beach'].add_paths_to(locations['Enumerable Canyon'],
                                              locations['Matzburg'])
    locations['Monkey Patch City'].add_paths_to(locations['Enumerable Canyon'],
                                                locations['Matzburg'],
                                                locations['Nil Town'])
    locations['Nil Town'].add_paths_to(locations['Monkey Patch City'],
                                       locations['Hash Crossing'])
    locations['Matzburg'].add_paths_to(locations['Duck Type Beach'],
                                       locations['Monkey Patch City'],
                                       locations['Hash Crossing'],
                                       locations['Dynamic Palisades'])
    locations['Hash Crossing'].add_paths_to(locations['Nil Town'],
                                            locations['Matzburg'],
                                            locations['Dynamic Palisades'])
    locations['Dynamic Palisades'].add_paths_to(locations['Matzburg'],
                                                locations['Hash Crossing'])
  rescue StandardError
    raise 'Locations not correctly initialized.'
  end

  # Start Running the simulation for a prospector
  def run
    puts "Rubyist #{@rubyist.id} starting in #{@current_location.name}."
    (1..@turns).each { |current_turn| prospect_location(current_turn) }
    # Print Results of run
    finished
  end

  # Method for Prospecting each location
  def prospect_location(current_turn)
    rubies_found = @current_location.find_rubies(@rndm)
    @rubyist.new_day
    # For each set of rubies found, print them out and increment amounts
    while rubies_found != [0, 0]
      _no_use = ruby_format_print(rubies_found)
      @rubyist.rubies_found(rubies_found[0], rubies_found[1])
      @rubyist.new_day
      rubies_found = @current_location.find_rubies(@rndm)
    end
    puts "\tFound no rubies or fake rubies in #{@current_location.name}."
    # After rubies in an area are exhausted, set new location and move on
    old_loc = @current_location
    @current_location = @current_location.next_location(@rndm)
    puts "Heading from #{old_loc.name} to #{@current_location.name}." if current_turn != @turns
  end

  # Prints found rubies with correct grammar
  def ruby_format_print(rubies)
    real_ruby_noun = (rubies[0] > 1 ? 'rubies' : 'ruby')
    fake_ruby_noun = (rubies[1] > 1 ? 'fake rubies' : 'fake ruby')
    pri = ''
    if rubies[0] > 0 && rubies[1] > 0
      pri += "\tFound #{rubies[0]} #{real_ruby_noun} and #{rubies[1]} #{fake_ruby_noun} in #{@current_location.name}.\n"
    elsif rubies[1].zero?
      pri += "\tFound #{rubies[0]} #{real_ruby_noun} in #{@current_location.name}.\n"
    else
      pri += "\tFound #{rubies[1]} #{fake_ruby_noun} in #{@current_location.name}.\n"
    end
    puts pri
    pri
  end

  # Prints the finishing statistics of a Rubyist's prospecting journey
  def finished
    day_noun = (@rubyist.days > 1 ? 'days' : 'day')
    real_noun = (@rubyist.real_ruby_count.zero? || @rubyist.real_ruby_count > 1 ? 'rubies' : 'ruby')
    fake_noun = (@rubyist.fake_ruby_count.zero? || @rubyist.fake_ruby_count > 1 ? 'fake rubies' : 'fake ruby')
    puts "After #{@rubyist.days} #{day_noun}, Rubyist #{@rubyist.id} found:"
    puts "\t#{@rubyist.real_ruby_count} #{real_noun}\n\t#{@rubyist.fake_ruby_count} #{fake_noun}"

    return_msg = ''
    return_msg = 'Going home empty-handed.' if @rubyist.real_ruby_count.zero?
    return_msg = 'Going home sad.' if @rubyist.real_ruby_count <= 9 && !@rubyist.real_ruby_count.zero?
    return_msg = 'Going home victorious!' if @rubyist.real_ruby_count > 9
    return_msg
  end
end
