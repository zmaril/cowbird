defmodule Cowbird.Postgres do
  def main(args) do
    IO.puts("Running postgres")
    IO.inspect(args)
    {opts,leftover,invalid} = OptionParser.parse(args,
      switches:
      [data_directory: :string,
       unix_directory: :string,
       nosync: :boolean],
      aliases: [D: :data_directory,
                F: :nosync,
                k: :unix_directory])
    # TOOD: figure out how to pull "-c", "listen_addresses=localhost" the right way
    listen_addresses = invalid |> Enum.at(0) |> elem(1) |> String.split("=") |> Enum.at(1)
    IO.inspect(opts)
    IO.inspect(leftover)
    IO.inspect(invalid)
    IO.inspect(listen_addresses)
  end
end 

