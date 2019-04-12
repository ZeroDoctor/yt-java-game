package com.zerulus.game.util;

public class Matrix {

    public static float[][] multiply(float[][] aMatrix, float[][] bMatrix) {
        if(aMatrix[0].length != bMatrix.length) {
            System.out.println("ERROR: A matrix columns does not equal B matrix rows");
            return identity(aMatrix);
        }

        float[][] outMatrix = new float[aMatrix.length][bMatrix[0].length];

		float temp;
		for(int i = 0; i < aMatrix.length; i++) {
			for(int j = 0; j < bMatrix[0].length; j++) {
				temp = 0;
				for(int k = 0; k < aMatrix[0].length; k++) {
					temp += aMatrix[i][k] * bMatrix[k][j]; 
				}
				outMatrix[i][j] = temp;
			}
		}

		return outMatrix;
    }

    public static float[][] multiply(float[][] matrix, float value) {
        float[][] outMatrix = new float[matrix.length][matrix[0].length];

        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[0].length; j++) {
                outMatrix[i][j] = value * matrix[i][j];
            }
        }

        return outMatrix;
    }

    public static float[][] add(float[][] aMatrix, float[][] bMatrix) {
        if(aMatrix.length != bMatrix.length || aMatrix[0].length != bMatrix[0].length ) {
            System.out.println("ERROR: A matrix dimensions does not equal B matrix dimensions");
            return identity(aMatrix);
        }

		float[][] outMatrix = new float[aMatrix.length][bMatrix[0].length];
        
		for(int i = 0; i < aMatrix.length; i++) {
			for(int j = 0; j < bMatrix[0].length; j++) {
				outMatrix[i][j] = aMatrix[i][j] + bMatrix[i][j];
			}
		}

		return outMatrix;
    }

    public static float[][] add(float[][] matrix, float value) {
        float[][] outMatrix = new float[matrix.length][matrix[0].length];

        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[0].length; j++) {
                outMatrix[i][j] = value + matrix[i][j];
            }
        }

        return outMatrix;
    }

    public static float[][] inverse(float[][] matrix) {
        float[][] inverse = new float[matrix.length][matrix[0].length];

        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[i].length; j++) {
                inverse[i][j] = (float) Math.pow(-1, i + j) * determinant(minor(matrix, i, j));
            }
        }

        float det = 1.0f / determinant(matrix);
		for (int i = 0; i < inverse.length; i++) {
			for (int j = 0; j <= i; j++) {
				float temp = inverse[i][j];
				inverse[i][j] = inverse[j][i] * det;
				inverse[j][i] = temp * det;
			}
		}

        return inverse;
    }

    public static float[][] transpose(float[][] matrix) {
		float[][] transpose = new float[matrix[0].length][matrix.length];

		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[i].length; j++)
				transpose[j][i] = matrix[i][j];
		return transpose;
    }

    private static float determinant(float[][] matrix) {
        if(matrix.length == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        }

        float det = 0;
        for(int i = 0; i < matrix[0].length; i++) {
            det += Math.pow(-1, i) * matrix[0][i] * determinant(minor(matrix, 0, i));
        }

        return det;
    }
    
    private static float[][] minor(float[][] matrix, int row, int col) {
        float[][] outMatrix = new float[matrix.length - 1][matrix[0].length - 1];

        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; i != row && j < matrix[0].length; j++) {
                if(j != col) {
                    outMatrix[i < row ? i : i - 1][j < col ? j : j - 1] = matrix[i][j];
                }
            }
        }

        return outMatrix;
    }

    private static float[][] identity(float[][] matrix) {
        float[][] outMatrix = new float[matrix.length][matrix.length];

        for(int i = 0; i < matrix.length; i++) {
            outMatrix[i][i] = 1;
        }

        return outMatrix;
    }

    public static void print(float[][] matrix) {
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + ",");
            }
            System.out.println();
        }
    }


}