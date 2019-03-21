# Rubyist Class, storing methods / details on rubyists
class Rubyist
  def initialize(id)
    @id = id
    @real_ruby_count = 0
    @fake_ruby_count = 0
    @days = 0
  end

  # Reader for accessing Rubyist instance variables
  attr_reader :id, :real_ruby_count, :fake_ruby_count, :days

  # Increments days spent prospecting
  def new_day
    @days += 1
  end

  # Increments count of real, fake rubies
  def rubies_found(num_real_rubies, num_fake_rubies)
    @real_ruby_count += num_real_rubies if num_real_rubies > -1
    @fake_ruby_count += num_fake_rubies if num_fake_rubies > -1
  end
end
