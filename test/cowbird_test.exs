defmodule CowbirdTest do
  use ExUnit.Case
  doctest Cowbird

  test "greets the world" do
    assert Cowbird.hello() == :world
  end
end
