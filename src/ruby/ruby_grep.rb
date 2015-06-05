#!/usr/bin/ruby

if ARGV.size > 0
  regexp = ARGV[0]
  regexp = Regexp.new(regexp)
  if ARGV.size > 1
    filename = ARGV[1]
    File.open(filename, "r") do |file|
      while line = file.gets
        puts line if regexp =~ line
      end
    end
  else
    while line = gets
      puts line if regexp =~ line
    end
  end
end