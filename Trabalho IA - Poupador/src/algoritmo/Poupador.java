package algoritmo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Agente Poupador
 * 
 * @author Ney Moura
 * @since 14/03/2015
 */
public class Poupador extends ProgramaPoupador {

	private final int MOVIMENTACAO_PARADO = 0;
	private final int MOVIMENTACAO_CIMA = 1;
	private final int MOVIMENTACAO_BAIXO = 2;
	private final int MOVIMENTACAO_DIREITA = 3;
	private final int MOVIMENTACAO_ESQUERDA = 4;

	private final int VISAO_INDISPONIVEL = -2;
	private final int VISAO_MUNDO_EXTERIOR = -1;
	private final int VISAO_CELULA_VAZIA = 0;
	private final int VISAO_PAREDE = 1;
	private final int VISAO_BANCO = 3;
	private final int VISAO_MOEDA = 4;
	private final int VISAO_PASTILHA = 5;
	private final int VISAO_POUPADOR = 100;
	private final int VISAO_LADRAO = 200;

	private final int OLFATO_VAZIO = 0;
	private final int OLFATO_UM_ATRAS = 1;
	private final int OLFATO_DOIS_ATRAS = 2;
	private final int OLFATO_TRES_ATRAS = 3;
	private final int OLFATO_QUATRO_ATRAS = 4;
	private final int OLFATO_CINCO_ATRAS = 5;

	private final int UTILIDADE_NULA = 0;

	private final boolean DEBUGA_ESTADOS = false;

	/**
	 * Executado a cada tick
	 * 
	 * @return int com a movimentacao a ser realizada
	 */
	public int acao() {

		// monta estado atual
		Estado estadoAtual = new Estado(sensor.getVisaoIdentificacao(),
				sensor.getAmbienteOlfatoLadrao(),
				sensor.getAmbienteOlfatoPoupador(), sensor.getNumeroDeMoedas(),
				sensor.getNumeroDeMoedasBanco(),
				sensor.getNumeroJogadasImunes(), sensor.getPosicao(),
				UTILIDADE_NULA, MOVIMENTACAO_PARADO);

		// gera os estados sucessores com base no atual
		List<Estado> estados = funcaoSucessora(estadoAtual);

		// ordena os estados considerando a maior utilidade primeiro
		Collections.sort(estados, new Comparator<Estado>() {

			@Override
			public int compare(Estado estado1, Estado estado2) {
				return Integer.compare(estado2.utilidade, estado1.utilidade);
			}

		});

		if (DEBUGA_ESTADOS) {
			
			System.out.println("***DEBUG***");
			
			for (Estado estado : estados) {
				System.out.println(estado.utilidade + ":"
						+ estado.movimentacaoRealizada);
			}

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// retorna a movimentacao realizada do estado com maior utilidade
		return estados.get(0).movimentacaoRealizada;

	}

	/**
	 * Classe estado representa o estado do mundo atual, contendo todas suas
	 * percepções além da movimentação realizada e sua utilidade
	 */
	private class Estado {

		protected int[] matrizVisao;

		protected int[] matrizOfaltivaLadroes;

		protected int[] matrizOfaltivaPoupadores;

		protected int moedas;

		protected int moedasNoBanco;

		protected int jogadasImunes;

		protected Point posicao;

		protected int utilidade;

		protected int movimentacaoRealizada;

		public Estado(int[] matrizVisao, int[] matrizOfaltivaLadroes,
				int[] matrizOfaltivaPoupadores, int moedas, int moedasNoBanco,
				int jogadasImunes, Point posicao, int utilidade,
				int movimentacaoRealizada) {
			super();
			this.matrizVisao = matrizVisao;
			this.matrizOfaltivaLadroes = matrizOfaltivaLadroes;
			this.matrizOfaltivaPoupadores = matrizOfaltivaPoupadores;
			this.moedas = moedas;
			this.moedasNoBanco = moedasNoBanco;
			this.jogadasImunes = jogadasImunes;
			this.posicao = posicao;
			this.utilidade = utilidade;
			this.movimentacaoRealizada = movimentacaoRealizada;
		}

	}

	/**
	 * A função sucessora calcula os estados sucessores de um dado estado.
	 * 
	 * @param estadoAtual
	 *            estado atual do problema
	 * @return List<Estado> representando todos os estados sucessores do
	 *         estadoAtual
	 */
	private List<Estado> funcaoSucessora(Estado estadoAtual) {

		List<Estado> estadosSucessores = new ArrayList<Estado>();

		for (int i = 0; i < MOVIMENTACAO_ESQUERDA + 1; i++) {

			Estado estadoSucessor = null;

			switch (i) {

			case MOVIMENTACAO_PARADO:

				estadoSucessor = calculaNovoEstado(MOVIMENTACAO_PARADO);

				estadosSucessores.add(estadoSucessor);

				break;

			case MOVIMENTACAO_CIMA:

				estadoSucessor = calculaNovoEstado(MOVIMENTACAO_CIMA);

				estadosSucessores.add(estadoSucessor);

				break;

			case MOVIMENTACAO_BAIXO:

				estadoSucessor = calculaNovoEstado(MOVIMENTACAO_BAIXO);

				estadosSucessores.add(estadoSucessor);

				break;

			case MOVIMENTACAO_DIREITA:

				estadoSucessor = calculaNovoEstado(MOVIMENTACAO_DIREITA);

				estadosSucessores.add(estadoSucessor);

				break;

			case MOVIMENTACAO_ESQUERDA:

				estadoSucessor = calculaNovoEstado(MOVIMENTACAO_ESQUERDA);

				estadosSucessores.add(estadoSucessor);

				break;

			default:
				break;
			}

		}

		return estadosSucessores;

	}

	/**
	 * Calcula um novo estado do mundo baseado em uma movimentação realizada
	 * 
	 * @param movimentacao
	 *            movimentação a ser realizada
	 * @return Estado com a nova posição e percepções.
	 */
	private Estado calculaNovoEstado(int movimentacao) {

		Estado estadoSucessor = new Estado(sensor.getVisaoIdentificacao(),
				sensor.getAmbienteOlfatoLadrao(),
				sensor.getAmbienteOlfatoPoupador(), sensor.getNumeroDeMoedas(),
				sensor.getNumeroDeMoedasBanco(),
				sensor.getNumeroJogadasImunes(), sensor.getPosicao(),
				UTILIDADE_NULA, movimentacao);

		switch (movimentacao) {

		case MOVIMENTACAO_PARADO:

			// do nothing
			
			for (int i = 0; i < 23; i++) {
				estadoSucessor.matrizVisao[i] = VISAO_INDISPONIVEL;
			}
			
			for (int i = 0; i < 7; i++) {
				estadoSucessor.matrizOfaltivaLadroes[i] = OLFATO_VAZIO;
				estadoSucessor.matrizOfaltivaPoupadores[i] = OLFATO_VAZIO;
			}

			break;

		case MOVIMENTACAO_CIMA:

			// considera apenas a visao e olfato do setor superior
			for (int i = 10; i <= 23; i++) {
				estadoSucessor.matrizVisao[i] = VISAO_INDISPONIVEL;
			}

			for (int i = 3; i <= 7; i++) {
				estadoSucessor.matrizOfaltivaLadroes[i] = OLFATO_VAZIO;
				estadoSucessor.matrizOfaltivaPoupadores[i] = OLFATO_VAZIO;
			}

			break;

		case MOVIMENTACAO_BAIXO:

			// considera apenas a visao e olfato do setor inferior
			for (int i = 0; i <= 13; i++) {
				estadoSucessor.matrizVisao[i] = VISAO_INDISPONIVEL;
			}

			for (int i = 0; i <= 4; i++) {
				estadoSucessor.matrizOfaltivaLadroes[i] = OLFATO_VAZIO;
				estadoSucessor.matrizOfaltivaPoupadores[i] = OLFATO_VAZIO;
			}

			break;

		case MOVIMENTACAO_DIREITA:

			// considera apenas a visao e olfato do setor direito
			estadoSucessor.matrizVisao[0] = VISAO_INDISPONIVEL;
			estadoSucessor.matrizVisao[1] = VISAO_INDISPONIVEL;
			estadoSucessor.matrizVisao[2] = VISAO_INDISPONIVEL;
			estadoSucessor.matrizVisao[5] = VISAO_INDISPONIVEL;
			estadoSucessor.matrizVisao[6] = VISAO_INDISPONIVEL;
			estadoSucessor.matrizVisao[7] = VISAO_INDISPONIVEL;
			estadoSucessor.matrizVisao[10] = VISAO_INDISPONIVEL;
			estadoSucessor.matrizVisao[11] = VISAO_INDISPONIVEL;
			estadoSucessor.matrizVisao[14] = VISAO_INDISPONIVEL;
			estadoSucessor.matrizVisao[15] = VISAO_INDISPONIVEL;
			estadoSucessor.matrizVisao[16] = VISAO_INDISPONIVEL;
			estadoSucessor.matrizVisao[19] = VISAO_INDISPONIVEL;
			estadoSucessor.matrizVisao[20] = VISAO_INDISPONIVEL;
			estadoSucessor.matrizVisao[21] = VISAO_INDISPONIVEL;

			estadoSucessor.matrizOfaltivaLadroes[0] = OLFATO_VAZIO;
			estadoSucessor.matrizOfaltivaLadroes[1] = OLFATO_VAZIO;
			estadoSucessor.matrizOfaltivaLadroes[3] = OLFATO_VAZIO;
			estadoSucessor.matrizOfaltivaLadroes[5] = OLFATO_VAZIO;
			estadoSucessor.matrizOfaltivaLadroes[6] = OLFATO_VAZIO;

			estadoSucessor.matrizOfaltivaPoupadores[0] = OLFATO_VAZIO;
			estadoSucessor.matrizOfaltivaPoupadores[1] = OLFATO_VAZIO;
			estadoSucessor.matrizOfaltivaPoupadores[3] = OLFATO_VAZIO;
			estadoSucessor.matrizOfaltivaPoupadores[5] = OLFATO_VAZIO;
			estadoSucessor.matrizOfaltivaPoupadores[6] = OLFATO_VAZIO;

			break;

		case MOVIMENTACAO_ESQUERDA:

			// considera apenas a visao e olfato do setor esquerdo
			estadoSucessor.matrizVisao[2] = VISAO_INDISPONIVEL;
			estadoSucessor.matrizVisao[3] = VISAO_INDISPONIVEL;
			estadoSucessor.matrizVisao[4] = VISAO_INDISPONIVEL;
			estadoSucessor.matrizVisao[7] = VISAO_INDISPONIVEL;
			estadoSucessor.matrizVisao[8] = VISAO_INDISPONIVEL;
			estadoSucessor.matrizVisao[9] = VISAO_INDISPONIVEL;
			estadoSucessor.matrizVisao[12] = VISAO_INDISPONIVEL;
			estadoSucessor.matrizVisao[13] = VISAO_INDISPONIVEL;
			estadoSucessor.matrizVisao[16] = VISAO_INDISPONIVEL;
			estadoSucessor.matrizVisao[17] = VISAO_INDISPONIVEL;
			estadoSucessor.matrizVisao[18] = VISAO_INDISPONIVEL;
			estadoSucessor.matrizVisao[21] = VISAO_INDISPONIVEL;
			estadoSucessor.matrizVisao[22] = VISAO_INDISPONIVEL;
			estadoSucessor.matrizVisao[23] = VISAO_INDISPONIVEL;

			estadoSucessor.matrizOfaltivaLadroes[1] = OLFATO_VAZIO;
			estadoSucessor.matrizOfaltivaLadroes[2] = OLFATO_VAZIO;
			estadoSucessor.matrizOfaltivaLadroes[4] = OLFATO_VAZIO;
			estadoSucessor.matrizOfaltivaLadroes[6] = OLFATO_VAZIO;
			estadoSucessor.matrizOfaltivaLadroes[7] = OLFATO_VAZIO;

			estadoSucessor.matrizOfaltivaPoupadores[1] = OLFATO_VAZIO;
			estadoSucessor.matrizOfaltivaPoupadores[2] = OLFATO_VAZIO;
			estadoSucessor.matrizOfaltivaPoupadores[4] = OLFATO_VAZIO;
			estadoSucessor.matrizOfaltivaPoupadores[6] = OLFATO_VAZIO;
			estadoSucessor.matrizOfaltivaPoupadores[7] = OLFATO_VAZIO;

			break;

		default:
			break;
		}

		estadoSucessor.utilidade = calculaUtilidade(estadoSucessor);

		return estadoSucessor;

	}

	/**
	 * Calcula a a utilidade do estado informado, considerando a visao e olfato
	 * 
	 * @param estado
	 * @return int com a utilidade do estado
	 */
	private int calculaUtilidade(Estado estado) {

		return utilidadeVisao(estado) + utilidadeOlfato(estado);
		// return (int) (Math.random()*5);

	}

	/**
	 * Calcula a utilidade visual de um dado estado
	 * 
	 * @param estado
	 * @return int com a utilidade visual
	 */
	private int utilidadeVisao(Estado estado) {

		int utilidadeVisual = 0;

		for (int i = 0; i < estado.matrizVisao.length; i++) {

			int contatoVisual = estado.matrizVisao[i];

			switch (contatoVisual) {

			case VISAO_INDISPONIVEL:

				continue;

			case VISAO_MUNDO_EXTERIOR:

				continue;

			case VISAO_CELULA_VAZIA:

				utilidadeVisual += +25;

				break;

			case VISAO_PAREDE:

				utilidadeVisual += -25;

				break;

			case VISAO_BANCO:

				if (estado.moedas >= 5) {
					utilidadeVisual += 100;
				} else {
					utilidadeVisual += 30;
				}

				break;

			case VISAO_MOEDA:

				utilidadeVisual += 100;

				break;

			case VISAO_PASTILHA:

				// adicionar '&& ladraoPerto'
				if (estado.jogadasImunes == 0 && estado.moedas >= 10) {
					utilidadeVisual += 50;
				} else {
					utilidadeVisual += 0;
				}

				break;

			case VISAO_POUPADOR:

				utilidadeVisual += -250;

				break;

			case VISAO_LADRAO:

				utilidadeVisual += -500;

				break;

			default:
				break;
			}

		}

		return utilidadeVisual;

	}

	/**
	 * Calcula a utilidade olfativa de um dado estado
	 * 
	 * @param estado
	 * @return int com a utilidade olfativa
	 */
	private int utilidadeOlfato(Estado estado) {

		int utilidadeOlfativa = 0;

		for (int i = 0; i < estado.matrizOfaltivaLadroes.length; i++) {

			int contatoOlfativoLadrao = estado.matrizOfaltivaLadroes[i];
			int contatoOlfativoPoupador = estado.matrizOfaltivaLadroes[i];

			switch (contatoOlfativoLadrao) {

			case OLFATO_VAZIO:

				utilidadeOlfativa += 50;

				break;

			case OLFATO_UM_ATRAS:

				utilidadeOlfativa += -100;

				break;

			case OLFATO_DOIS_ATRAS:

				utilidadeOlfativa += -90;

				break;

			case OLFATO_TRES_ATRAS:

				utilidadeOlfativa += -80;

				break;

			case OLFATO_QUATRO_ATRAS:

				utilidadeOlfativa += -50;

				break;

			case OLFATO_CINCO_ATRAS:

				utilidadeOlfativa += -40;

				break;

			default:
				break;
			}

			switch (contatoOlfativoPoupador) {

			case OLFATO_VAZIO:

				utilidadeOlfativa += 50;

				break;

			case OLFATO_UM_ATRAS:

				utilidadeOlfativa += -100;

				break;

			case OLFATO_DOIS_ATRAS:

				utilidadeOlfativa += -90;

				break;

			case OLFATO_TRES_ATRAS:

				utilidadeOlfativa += -80;

				break;

			case OLFATO_QUATRO_ATRAS:

				utilidadeOlfativa += -50;

				break;

			case OLFATO_CINCO_ATRAS:

				utilidadeOlfativa += -40;

				break;

			default:
				break;
			}

		}

		return utilidadeOlfativa;

	}

}