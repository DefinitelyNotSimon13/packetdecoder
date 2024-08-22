package com.work

fun interface CalculationStrategy {
    fun calculate(body: ParentBody): Long
}

fun retrieveCalcStrategy(typeId: Byte): CalculationStrategy {
    return when (typeId.toInt()) {
        0 -> AdditionStrategy()
        1 -> MultiplicationStrategy()
        2 -> MinimumStrategy()
        3 -> MaximumStrategy()
        5 -> GreaterThenStrategy()
        6 -> LassThenStrategy()
        7 -> EqualsStrategy()
        else -> throw IllegalArgumentException("Invalid typeId: $typeId")
    }
}

class AdditionStrategy : CalculationStrategy {
    override fun calculate(body: ParentBody): Long {
        val childValues = body.children.map(Packet::reduce)
        return childValues.sum()
    }
}

class MultiplicationStrategy : CalculationStrategy {
    override fun calculate(body: ParentBody): Long {
        val childValues = body.children.map(Packet::reduce)
        return childValues.reduce { acc, i -> acc * i }
    }
}

class MinimumStrategy : CalculationStrategy {
    override fun calculate(body: ParentBody): Long {
        val childValues = body.children.map(Packet::reduce)
        return childValues.min()
    }
}

class MaximumStrategy : CalculationStrategy {
    override fun calculate(body: ParentBody): Long {
        val childValues = body.children.map(Packet::reduce)
        return childValues.max()
    }
}

class GreaterThenStrategy : CalculationStrategy {
    override fun calculate(body: ParentBody): Long {
        val children = body.children.map(Packet::reduce)
        require(children.size == 2) { "GreaterThenStrategy can only be used with 2 children" }
        return if (children[0] > children[1]) 1 else 0

    }
}

class LassThenStrategy : CalculationStrategy {
    override fun calculate(body: ParentBody): Long {
        val children = body.children.map(Packet::reduce)
        require(children.size == 2) { "LassThenStrategy can only be used with 2 children" }
        return if (children[0] < children[1]) 1 else 0
    }
}

class EqualsStrategy : CalculationStrategy {
    override fun calculate(body: ParentBody): Long {
        val children = body.children.map(Packet::reduce)
        require(children.size == 2) { "EqualsStrategy can only be used with 2 children" }
        return if (children[0] == children[1]) 1 else 0
    }
}