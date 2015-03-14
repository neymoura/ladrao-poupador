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
@SuppressWarnings("unused")
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

		/*
		 * for (Estado estado : estados) { System.out.println(estado.utilidade +
		 * ":" + estado.movimentacaoRealizada); }
		 * 
		 * try { Thread.sleep(10000); } catch (InterruptedException e) {
		 * e.printStackTrace(); }
		 */

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

		Point posicaoOriginal = sensor.getPosicao();

		Point novaPosicao = posicaoOriginal;

		switch (movimentacao) {

		case MOVIMENTACAO_PARADO:

			// novaPosicao = posicaoOriginal;

			break;

		case MOVIMENTACAO_CIMA:

			novaPosicao.y += 1;

			break;

		case MOVIMENTACAO_BAIXO:

			novaPosicao.y -= 1;

			break;

		case MOVIMENTACAO_DIREITA:

			novaPosicao.x += 1;

			break;

		case MOVIMENTACAO_ESQUERDA:

			novaPosicao.x -= 1;

			break;

		default:
			break;
		}

		sensor.setPosicao(novaPosicao);

		Estado estadoSucessor = new Estado(sensor.getVisaoIdentificacao(),
				sensor.getAmbienteOlfatoLadrao(),
				sensor.getAmbienteOlfatoPoupador(), sensor.getNumeroDeMoedas(),
				sensor.getNumeroDeMoedasBanco(),
				sensor.getNumeroJogadasImunes(), sensor.getPosicao(),
				UTILIDADE_NULA, movimentacao);

		estadoSucessor.utilidade = calculaUtilidade(estadoSucessor);

		sensor.setPosicao(posicaoOriginal);

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

				//utilidadeVisual += -1;
				continue;

			case VISAO_MUNDO_EXTERIOR:

				//utilidadeVisual += -1;
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
					utilidadeVisual += 10;
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
//		return 0;
		return (int) (Math.random() * 10);
	}

}