package app.tutorial2;

import ec.*;
import ec.simple.*;
import ec.util.*;
import ec.vector.*;

public class AddSubtract extends Problem implements SimpleProblemForm {

	@Override
	public void evaluate(final EvolutionState state, final Individual ind, final int subpopulation,
			final int threadnum) {
		if (ind.evaluated)
			return;

		if (!(ind instanceof IntegerVectorIndividual))
			state.output.fatal("Whoa!  It's not a IntegerVectorIndividual!!!", null);

		IntegerVectorIndividual ind2 = (IntegerVectorIndividual) ind;
		// Now we add/subtract genes to get our fitness:
		int rawfitness = 0;
		for (int x = 0; x < ind2.genome.length; x++)
			if (x % 2 == 0)
				rawfitness += ind2.genome[x];
			else
				rawfitness -= ind2.genome[x];

		// We finish by taking the ABS of rawfitness as mentioned. By the way,
		// in SimpleFitness, fitness values must be set up so that 0 is <= the
		// worst
		// fitness and +infinity is >= the ideal possible fitness. Our raw
		// fitness
		// value here satisfies this.
		if (rawfitness < 0)
			rawfitness = -rawfitness;
		if (!(ind2.fitness instanceof SimpleFitness))
			state.output.fatal("Whoa!  It's not a SimpleFitness!!!", null);
		((SimpleFitness) ind2.fitness).setFitness(state,
				// what the heck, lets normalize the fitness for genome length
				((double) rawfitness) / ind2.genome.length,
				// ... is the individual ideal? Indicate here...
				false);
		ind2.evaluated = true;
	}

}
