#!/usr/bin/ruby

class Tree
  attr_accessor :children, :node_name

  def initialize(data = {})
    name = nil
    @children = []
    if data
      if data.instance_of? String
        name = data
      end
      if data.instance_of? Hash
        pair = data.first
        if pair
          name = pair[0]
          pair[1].each_pair {|key, value|
            @children.push Tree.new({key => value})
          }
        end
      end
    end
    @node_name = name
  end

  def visit_all(&block)
    visit &block
    children.each {|c| c.visit_all &block} if children
  end

  def visit(&block)
    block.call self
  end
end

tree = Tree.new({"granddad" => {"dad" => {"son" => {}, "daughter" => {}}, "uncle" => {"niece" => {}, "nephew" => {}}}})

tree.visit_all {|node| puts node.node_name}
