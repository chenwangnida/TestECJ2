package app.tutorial2;

import ec.vector.*;
import ec.*;
import ec.util.*;

/*
 * OurMutatorPipeline.java
 */

/**
 * OurMutatorPipeline is a BreedingPipeline which negates the sign of genes. The
 * individuals must be IntegerVectorIndividuals. Because we're lazy, we'll use
 * the individual's species' mutation-probability parameter to tell us whether
 * or not to mutate a given gene.
 * 
 * <p>
 * <b>Typical Number of Individuals Produced Per <tt>produce(...)</tt>
 * call</b><br>
 * (however many its source produces)
 * 
 * <p>
 * <b>Number of Sources</b><br>
 * 1
 */
public class OurMutatorPipeline extends BreedingPipeline {

	// used only for our default base
	public static final String P_OURMUTATION = "our-mutation";

	// We have to specify a default base, even though we never use it
	public Parameter defaultBase() {
		return VectorDefaults.base().push(P_OURMUTATION);
	}

	public static final int NUM_SOURCES = 1;

	// Return 1 -- we only use one source
	public int numSources() {
		return NUM_SOURCES;
	}

	public int produce(final int min, final int max, final int start, final int subpopulation, final Individual[] inds,
			final EvolutionState state, final int thread) {
		//We get those individuals by asking our source for them
		int n = sources[0].produce(min, max, start, subpopulation, inds, state, thread);

		// should we bother?
		if (!state.random[thread].nextBoolean(likelihood))
			// DON'T produce children from source -- we already did
			return reproduce(n, start, subpopulation, inds, state, thread, false);

		if (!(sources[0] instanceof BreedingPipeline))
			for (int q = start; q < n + start; q++)
				inds[q] = (Individual) (inds[q].clone());

		if (!(inds[start] instanceof IntegerVectorIndividual))
			// uh oh, wrong kind of individual
			state.output.fatal(
					"OurMutatorPipeline didn't get an " + "IntegerVectorIndividual.  The offending individual is "
							+ "in subpopulation " + subpopulation + " and it's:" + inds[start]);
		IntegerVectorSpecies species = (IntegerVectorSpecies) (inds[start].species);

		for (int q = start; q < n + start; q++) {
			IntegerVectorIndividual i = (IntegerVectorIndividual) inds[q];
			for (int x = 0; x < i.genome.length; x++)
				//we'll flip a coin of per-gene mutationProbability
				if (state.random[thread].nextBoolean(species.mutationProbability(x)))
					i.genome[x] = -i.genome[x];
			// it's a "new" individual, so it's no longer been evaluated
			i.evaluated = false;
		}
		return n;
	}
}
