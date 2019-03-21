# Parses Arguments and returns
class Argparser
  def correct?(args)
    all_args = args.map(&:to_i)
    if all_args.size != 3 || all_args[1] < 1 || all_args[2] < 1 ||
       !all_args[0].is_a?(Integer) || !all_args[1].is_a?(Integer) ||
       !all_args[2].is_a?(Integer)
      puts "Usage:\nruby ruby_rush.rb *seed* *num_prospectors *num_turns*"
      puts '*seed* should be an integer'
      puts '*num_prospectors* should be a non-negative integer'
      puts '*num_turns* should be a non-negative integer'
      false
    else
      true
    end
  rescue StandardError
    puts "Usage:\nruby ruby_rush.rb *seed* *num_prospectors *num_turns*"
    puts '*seed* should be an integer'
    puts '*num_prospectors* should be a non-negative integer'
    puts '*num_turns* should be a non-negative integer'
    false
  end
end
