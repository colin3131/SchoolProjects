# Require minitest and rubyist to run test
require 'minitest/autorun'
require_relative 'rubyist'

# All tests for the Rubyist Class
class RubyistTest < Minitest::Test
  # UNIT TEST FOR METHOD initialize(id)
  # Equivalence Class:
  # (id) -> .id -> returns id

  # If id is initialized to 1, referencing id will return 1
  def test_initialize_id
    prospector = Rubyist.new(1)
    assert_equal prospector.id, 1
  end

  # UNIT TEST FOR METHOD new_day
  # Equivalence Class:
  # .new_day -> .days -> returns 1

  # If rubyist is initialized and new_day is called, .days will return 1
  def test_new_day
    prospector = Rubyist.new(1)
    prospector.new_day
    assert_equal prospector.days, 1
  end

  # UNIT TEST FOR METHOD rubies_found(real, fake)
  # Equivalence Classes:
  # real, fake = 0..INFINITY -> .real/fake_ruby_count return count + real/fake
  # real, fake = -INFINITY..-1 -> returns counts + 0

  # If positive real and fake rubies are passed in, they're added to the total count
  def test_rubies_found_positive
    prospector = Rubyist.new(1)
    prospector.rubies_found(2, 2)
    assert prospector.real_ruby_count == 2 && prospector.fake_ruby_count == 2
  end

  # If negative real and fake rubies are passed in, they're not added to the total count
  # EDGE CASE
  def test_rubies_found_negative
    prospector = Rubyist.new(1)
    prospector.rubies_found(-2, -2)
    assert prospector.real_ruby_count.zero? && prospector.fake_ruby_count.zero?
  end
end
