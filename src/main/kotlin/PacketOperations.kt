package com.work

import java.math.BigInteger

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
    override fun calculate(body: ParentBody): Long =
        body.children.map(Packet::calculate).sum()
}

class MultiplicationStrategy : CalculationStrategy {
    override fun calculate(body: ParentBody): Long {
        val childValues = body.children.map(Packet::calculate)
        val product = childValues.map { BigInteger.valueOf(it) }
            .reduce { acc, value -> acc.multiply(value) }
        if (product > BigInteger.valueOf(Long.MAX_VALUE)) {
            throw ArithmeticException("Multiplication overflow")
        }
        return product.toLong()
    }
}

class MinimumStrategy : CalculationStrategy {
    override fun calculate(body: ParentBody): Long = body.children.minOf(Packet::calculate)
}

class MaximumStrategy : CalculationStrategy {
    override fun calculate(body: ParentBody): Long = body.children.maxOf(Packet::calculate)
}

class GreaterThenStrategy : CalculationStrategy {
    override fun calculate(body: ParentBody): Long {
        val children = body.children.map(Packet::calculate)
        require(children.size == 2) { "GreaterThenStrategy can only be used with 2 children" }
        return if (children[0] > children[1]) 1 else 0
    }
}

class LassThenStrategy : CalculationStrategy {
    override fun calculate(body: ParentBody): Long {
        val children = body.children.map(Packet::calculate)
        require(children.size == 2) { "LassThenStrategy can only be used with 2 children" }
        return if (children[0] < children[1]) 1 else 0
    }
}

class EqualsStrategy : CalculationStrategy {
    override fun calculate(body: ParentBody): Long {
        val children = body.children.map(Packet::calculate)
        require(children.size == 2) { "EqualsStrategy can only be used with 2 children" }
        return if (children[0] == children[1]) 1 else 0
    }
}