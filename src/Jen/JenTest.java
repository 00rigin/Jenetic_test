package Jen;

import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.Chromosome;
import io.jenetics.IntegerGene;
import io.jenetics.Optimize;
import io.jenetics.RouletteWheelSelector;
import io.jenetics.TournamentSelector;
import io.jenetics.DoubleGene;
import io.jenetics.EliteSelector;
import io.jenetics.Genotype;
import io.jenetics.IntegerChromosome;
import io.jenetics.engine.Engine;
import io.jenetics.engine.Limits;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.util.Factory;
import io.jenetics.util.IntRange;

import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.Random;

import Jen.CustomSelector;

public class JenTest{
	
//	static double weightA = 0.5;
//	static double weightB = 0.5;
//	static boolean flag = false;
//	
//	private static double calcTrTime() { //�ӽ�
//		Random r = new Random();
//		return r.nextDouble();
//	}
//	
//	private static double calcSuccessRate() { // �ӽ�
//		Random r = new Random();
//		return r.nextDouble();
//	}
//	
//	private static Double calcAvgValue(Chromosome<IntegerGene> gt) {
//
//		double avgTrTime = 0;
//		double avgSuccessRate = 0;
//		
//		
//		for(int i = 0; i<gt.length(); i++) {
//			avgTrTime+=calcTrTime(); // �Լ� ���� �ʿ�
//			avgSuccessRate+=calcSuccessRate(); // �Լ� ���� �ʿ�
//		}
//		
//		avgTrTime = avgTrTime/gt.length();
//		avgSuccessRate = avgSuccessRate/gt.length();
//		
//		return weightA*avgSuccessRate - weightB*avgTrTime;
//	}
	
//	private static Double calcValue(Genotype<IntegerGene> gt) {
//		double trTime = 0;
//		double sRate = 0;
//		
//		trTime = calcTrTime();
//		sRate = calcSuccessRate();
//		
//		
//		return weightA*sRate - weightB*trTime;
//		// �� ������ �̰� maximize �ϴ°�!
//	}
	
	// ���� ����. �� gene�� ���̸� �ִ�ȭ �ϵ��� ¥�°�.
	private static Double calcValue(Genotype<IntegerGene> gt) {
		// gt�� [[[gene],[gene],.....,[gene]]] �÷� �����Ǿ�����
		
		Double res=(double) 0;
		
		for(int i = 1; i<gt.get(0).length(); i++) {
			res += (gt.get(0).get(i).intValue()-gt.get(0).get(i-1).intValue());
		}
		res = res/gt.get(0).length();
		return res;
		
	}
	
	// select method implementation
	
	
	
	public static void main(String[] args) {
		
		
		// Genetype �����ϱ�. �ϴ� integer�� ������ chromosome
		Factory<Genotype<IntegerGene>> gt = 
				Genotype.of(IntegerChromosome.of(0,9, 10)); // min,max,len ���� ���� Integer chromosome ����.....
		System.out.println("start :" + ((Genotype<IntegerGene>) gt).get(0));
		
		System.out.println("Value : " + calcValue((Genotype<IntegerGene>) gt));
		
		// ���� ȯ�� ����
		Engine<IntegerGene, Double> engine = Engine
				.builder(
						JenTest::calcValue, 
						gt)
				.populationSize(500)
				.survivorsSelector(new CustomSelector<>()) // ���⼭ custom selector ����ϱ� 
				.optimize(Optimize.MAXIMUM)
				.build();
		
		// ����
		Genotype<IntegerGene> result = engine.stream()
				.limit(100)
				.collect(EvolutionResult.toBestGenotype());
		
		System.out.println("Result :" + result);
		System.out.println("Value : " + calcValue((Genotype<IntegerGene>) result));
        System.out.println("--------------------------");
		

	}

}

//public class JenTest {
//    // Definition of the fitness function.
//    private static Double eval(final Genotype<DoubleGene> gt) {
//        final double x = gt.gene().doubleValue();
//        return cos(0.5 + sin(x))*cos(x);
//    }
//
//    public static void main(String[] args) {
//        // Create/configuring the engine via its builder.
//        final Engine<DoubleGene, Double> engine = Engine
//            .builder(
//                JenTest::eval,
//                DoubleChromosome.of(0.0, 2.0*PI))
//            .populationSize(500)
//            .optimize(Optimize.MINIMUM)
//            .alterers(
//                new Mutator<>(0.03),
//                new MeanAlterer<>(0.6))
//            .build();
//
//        // Execute the GA (engine).
//        final Phenotype<DoubleGene, Double> result = engine.stream()
//             // Truncate the evolution stream if no better individual could
//             // be found after 5 consecutive generations.
//            .limit(bySteadyFitness(5))
//             // Terminate the evolution after maximal 100 generations.
//            .limit(100)
//            .collect(toBestPhenotype());
//     }
// }




//public class main {
//    // 2.) Definition of the fitness function.
//    private static Integer eval(Genotype<BitGene> gt) {
//        return gt.chromosome()
//            .as(BitChromosome.class)
//            .bitCount();
//    }
//
//    public static void main(String[] args) {
//        // 1.) Define the genotype (factory) suitable
//        //     for the problem.
//    	for(int i = 0; i<10 ; i++) {
//    		Factory<Genotype<BitGene>> gtf =
//    	            Genotype.of(BitChromosome.of(10, 0.5));
//    	        
//    	        System.out.println("Start with :" + gtf);
//
//    	        // 3.) Create the execution environment.
//    	        Engine<BitGene, Integer> engine = Engine
//    	            .builder(main::eval, gtf)
//    	            .build();
//
//    	        // 4.) Start the execution (evolution) and
//    	        //     collect the result.
//    	        Genotype<BitGene> result = engine.stream()
//    	            .limit(100)
//    	            .collect(EvolutionResult.toBestGenotype());
//
//    	        System.out.println("Hello World :" + result);
//    	        System.out.println("--------------------------");
//    		
//    	}
////        Factory<Genotype<BitGene>> gtf =
////            Genotype.of(BitChromosome.of(10, 0.5));
////        
////        System.out.println("Start with : \n" + gtf);
////
////        // 3.) Create the execution environment.
////        Engine<BitGene, Integer> engine = Engine
////            .builder(main::eval, gtf)
////            .build();
////
////        // 4.) Start the execution (evolution) and
////        //     collect the result.
////        Genotype<BitGene> result = engine.stream()
////            .limit(100)
////            .collect(EvolutionResult.toBestGenotype());
////
////        System.out.println("Hello World:\n" + result);
//    }
//}

