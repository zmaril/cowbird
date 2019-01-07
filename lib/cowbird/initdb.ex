defmodule Cowbird.Initdb do
  def main(args) do
    IO.puts("Running Initdb")
    IO.inspect(args)
    {opts,_,_}= OptionParser.parse(args,
      switches:
      [directory: :string,
       noclean: :boolean,
       nosync: :boolean],
      aliases: [D: :directory])
    File.mkdir(opts[:directory])
  end
end 
