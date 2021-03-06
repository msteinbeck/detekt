package io.gitlab.arturbosch.detekt.api

import org.jetbrains.kotlin.psi.KtFile

/**
 * A rule set is a collection of rules and must be defined within a rule set provider implementation.
 *
 * @author Artur Bosch
 */
class RuleSet(val id: String, val rules: List<Rule>) {

	init {
		validateIdentifier(id)
	}

	/**
	 * Iterates over the given kotlin file list and calling the accept method.
	 */
	fun acceptAll(files: List<KtFile>): Pair<String, List<Finding>> {
		return id to files.flatMap { accept(it) }
	}

	/**
	 * Visits given file with all rules of this rule set, returning a list
	 * of all code smell findings.
	 */
	fun accept(file: KtFile): List<Finding> {
		val findings: MutableList<Finding> = mutableListOf()
		rules.forEach {
			it.visit(file)
			findings += it.findings
		}
		return findings
	}

}