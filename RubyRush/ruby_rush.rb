require_relative 'sim'
require_relative 'rubyist'
require_relative 'argparser'
# Check if args are correct, return 1 if they arent
exit 1 unless Argparser.new.correct?(ARGV)
# Check passes, assign variables
seed = ARGV[0].to_i
num_prospectors = ARGV[1].to_i
num_turns = ARGV[2].to_i
# Seeded Random init
random_generator = Random.new(seed)
# Start simulation loop
(1..num_prospectors).each do |rubyist_id|
  current_prospector = Rubyist.new(rubyist_id)
  simulator = Sim.new(current_prospector, num_turns, random_generator)
  simulator.run
end
exit 0
