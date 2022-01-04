package Jen;

import static java.lang.Math.min;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import io.jenetics.Gene;
import io.jenetics.Optimize;
import io.jenetics.Phenotype;
import io.jenetics.Selector;
import io.jenetics.util.ISeq;
import io.jenetics.util.MSeq;
import io.jenetics.util.Seq;


public final class CustomSelector<G extends Gene<?, G>,	C extends Comparable<? super C>> implements Selector<G, C> {

	private final int _n;

	/**
	 * Create a new {@code TruncationSelector} object, where the worst selected
	 * individual has rank {@code n}. This means, if you want to select
	 * {@code count} individuals, the worst selected individual has rank
	 * {@code n}. If {@code count > n}, the selected population will contain
	 * <em>duplicate</em> individuals.
	 *
	 * @since 3.8
	 *
	 * @param n the worst rank of the selected individuals
	 * @throws IllegalArgumentException if {@code n < 1}
	 */
	public CustomSelector(final int n) {
		if (n < 1) {
			throw new IllegalArgumentException(format(
				"n must be greater or equal 1, but was %d.", n
			));
		}

		_n = n;
	}

	/**
	 * Create a new TruncationSelector object.
	 */
	public CustomSelector() {
		this(Integer.MAX_VALUE);
	}

	/**
	 * This method sorts the population in descending order while calculating
	 * the selection probabilities. If the selection size is greater the the
	 * population size, the whole population is duplicated until the desired
	 * sample size is reached.
	 *
	 * @throws NullPointerException if the {@code population} or {@code opt} is
	 *         {@code null}.
	 */
	@Override
	public ISeq<Phenotype<G, C>> select(final Seq<Phenotype<G, C>> population,final int count,final Optimize opt) {
		
		requireNonNull(population, "Population");
		requireNonNull(opt, "Optimization");
		if (count < 0) {
			throw new IllegalArgumentException(format(
				"Selection count must be greater or equal then zero, but was %s",
				count
			));
		}
		
		final MSeq<Phenotype<G, C>> selection = MSeq.ofLength(population.isEmpty() ? 0 : count);

		if (count > 0 && !population.isEmpty()) {
			final MSeq<Phenotype<G, C>> copy = population.asISeq().copy();
//			System.out.println(copy.getClass().getName());
			System.out.println("0 : "+copy.asList().get(0).genotype());
			System.out.println("0 : "+copy.asList().get(0).genotype().get(0).get(0).toString());
			System.out.println("1 : "+copy.asList().get(1));
			System.out.println("1 : "+copy.asList().get(5));
//			final List<Integer> list = new ArrayList<>();
//			list = copy.asList().get(0).get(0);
			copy.sort((a, b) ->
				opt.<C>descending().compare(a.fitness(), b.fitness()));

			int size = count;
			do {
				final int length = min(min(copy.size(), size), _n);
				for (int i = 0; i < length; ++i) {
					selection.set((count - size) + i, copy.get(i));
				}

				size -= length;
			} while (size > 0);
		}

		return selection.toISeq();
	}

	@Override
	public String toString() {
		return getClass().getName();
	}

}