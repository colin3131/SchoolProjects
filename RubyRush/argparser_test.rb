# Require minitest and argparser to run test
require 'minitest/autorun'
require_relative 'argparser'

# All tests for the Argparser Class
class ArgparserTest < Minitest::Test
  # UNIT TESTS FOR METHOD correct?(args)
  # Equivalence Class:
  # args = [-INF..INF, 1..INF, 1..INF] -> returns true
  # args = [-INF..INF, 1..INF, 1..INF, 1..INF] -> returns false, prints Correct Usage
  # args = [-INF..INF, -INF..0, -INF..0] -> returns false, prints Correct Usage

  # If correct parameters for an argument are passed in, returns true
  def test_correct_true
    a_parse = Argparser.new
    assert a_parse.correct?([-20, 5, 1])
  end

  # If too many parameters are passed in, returns false
  # EDGE CASE
  def test_correct_extra_arg
    a_parse = Argparser.new
    assert !a_parse.correct?([-20, 5, 1, 0])
  end

  # If negative values are passed in for prospectors or turns, prints Correct Usage and returns false
  # EDGE CASE
  def test_correct_neg_val_print
    a_parse = Argparser.new
    assert_output(nil) do
      _unused = a_parse.correct?([-20, -5, 0])
    end
  end
end
