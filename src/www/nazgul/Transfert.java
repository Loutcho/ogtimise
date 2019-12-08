package www.nazgul;

public class Transfert {

	public static void main(String args[]) {

		double volumeObjectif = 3000000.0;
		final int nbSources = 6;
		Source[] source = new Source[nbSources];
		source[0] = new Source(516000.0, 40300.0, 30.0 / 60.0);
		source[1] = new Source(438000.0, 38800.0, 40.0 / 60.0);
		source[2] = new Source(84000.0, 38000.0, 0.0 / 60.0);
		source[3] = new Source(369000.0, 40300.0, 50.0 / 60.0);
		source[4] = new Source(300000.0, 31200.0, 60.0 / 60.0);
		source[5] = new Source(739000.0, 40300.0, 70.0 / 60.0);

		int nbConfigurations = (1 << nbSources);
		// La configuration = la liste des sources qui contribuent.
		// Il y a nbConfigurations = 2 ^ nbSources possibilités de configuration.
		// Les configurations sont numérotées de 0 à 2^nbSources - 1, comme suit :
		// Bit n°i : la source i contribue-t-elle ? 0 = non, 1 = oui.
		// Exemples :
		// - configuration 000000: aucune source ne contribue -> inutile, on ne la calcule même pas.
		// - configuration 000001: la source 0 contribue, pas les autres
		//   ...
		// - configuration 010101: les sources 0, 2, 4 contribuent, mais pas les sources 1, 3, 5
		//   ...
		// - configuration 111111: toutes les sources contribuent

		for (int configuration = 1; configuration < nbConfigurations; configuration ++) {
			traiterConfiguration(configuration, source, volumeObjectif);
		}
		/*
		traiterConfiguration(0x3F, source, volumeObjectif);
		 */
	}

	private static void traiterConfiguration(int configuration, Source[] source, double volumeObjectif) {
		double dtMax = dureeTransfertMax(configuration, source); // durée de transfert la plus longue
		double vMin = volumeTransfereTotal(configuration, source, dtMax, 0.0); // ce qu'on obtient au mieux si l'on n'attend pas pour procéder au transfert de plus longue durée
		double dtAttente; // le temps qu'il faut attendre avant de commencer le transfert de durée la plus longue
		double t; // l'instant commun où tous les transferts depuis les sources qui contribuent finissent

		if (vMin >= volumeObjectif) {
			dtAttente = 0.0;
		} else {
			dtAttente = (volumeObjectif
					- volumeTotal(configuration, source, 0.0)
					- volumeBonusTotal(configuration, source, dtMax)) / (debitTotal(configuration, source));
		}
		t = dtMax + dtAttente;
		System.out.println(String.format("config %s: dtMax=%f, vMin=%f, dtAttente=%f, t=%f, vérification=%f, %s",
				Integer.toBinaryString(configuration),
				dtMax,
				vMin,
				dtAttente,
				t,
				volumeTransfereTotal(configuration, source, dtMax, dtAttente),
				listeInstantsTransfert(configuration, source, dtMax, dtAttente)
		));
	}

	// calcul des volumes bonus = la somme des volumes continués d'être produits
	// entre le moment où la 1ère contribution est partie
	// et le moment où chaque [autre] contribution part
	private static double volumeBonusTotal(int configuration, Source[] source, double dtMax) {
		double total = 0.0;
		for (int iSource = 0; iSource < source.length; iSource ++) {
			if ((configuration & (1 << iSource)) != 0) {
				total += source[iSource].debit * (dtMax - source[iSource].dureeTransfert);
			}
		}
		return total;
	}

	// calcul du temps d'attente incompressible pour regrouper tous les volumes générés par les sources qui contribuent
	private static double dureeTransfertMax(int configuration, Source[] source) {
		double dtMax = Double.NEGATIVE_INFINITY;
		for (int iSource = 0; iSource < source.length; iSource ++) {
			if ((configuration & (1 << iSource)) != 0) {
				if (source[iSource].dureeTransfert > dtMax) {
					dtMax = source[iSource].dureeTransfert;
				}
			}
		}
		return dtMax;
	}

	// volume généré par une source donnée à un instant donné (avant transfert)
	private static double volume(Source source, double instant) {
		return source.volumeInitial + source.debit * instant;
	}

	// volume généré par l'ensemble des sources qui contribuent, à un instant donné (avant transfert)
	private static double volumeTotal(int configuration, Source[] source, double instant) {
		double total = 0.0;
		for (int iSource = 0; iSource < source.length; iSource ++) {
			if ((configuration & (1 << iSource)) != 0) {
				total += volume(source[iSource], instant);
			}
		}
		return total;
	}

	// débit total de l'ensemble des sources qui contribuent
	private static double debitTotal(int configuration, Source[] source) {
		double total = 0.0;
		for (int iSource = 0; iSource < source.length; iSource ++) {
			if ((configuration & (1 << iSource)) != 0) {
				total += source[iSource].debit;
			}
		}
		return total;
	}

	// calcul de l'instant du transfert pour une source donnée
	private static double instantTransfert(Source source, double dtMax, double dtAttente) {
		return dtMax + dtAttente - source.dureeTransfert;
	}

	// calcul du volume transféré pour une source donnée
	private static double volumeTransfere(Source source, double dtMax, double dtAttente) {
		double tTransfert = instantTransfert(source, dtMax, dtAttente);
		return volume(source, tTransfert);
	}

	// calcul du volume transféré total
	private static double volumeTransfereTotal(int configuration, Source[] source, double dtMax, double dtAttente) {
		double total = 0.0;
		for (int iSource = 0; iSource < source.length; iSource ++) {
			if ((configuration & (1 << iSource)) != 0) {
				total += volumeTransfere(source[iSource], dtMax, dtAttente);
			}
		}
		return total;
	}

	private static String listeInstantsTransfert(int configuration, Source[] source, double dtMax, double dtAttente) {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		for (int iSource = 0; iSource < source.length; iSource ++) {
			if (iSource > 0) {
				sb.append(", ");
			}
			if ((configuration & (1 << iSource)) != 0) {
				Double tTransfert = instantTransfert(source[iSource], dtMax, dtAttente);
				sb.append(tTransfert.toString());

			} else {
				sb.append("-");
			}
		}
		sb.append(")");
		return sb.toString();
	}
}
