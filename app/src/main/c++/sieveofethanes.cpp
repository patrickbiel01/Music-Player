//
// Created by Patrick on 2018-01-27.
//

#include "sieveofethanes.h"
#include "math.h"
#include <iostream>


int main(){

	const int PRIME_LIMIT = 10000;

	bool primes[PRIME_LIMIT];

	//Set a values in array to true
	for (int i = 0; i < sizeof(primes); i++) {
		primes[i] = true;
	}

	int nthRoot = (int)sqrt(PRIME_LIMIT);

	//Set array to prime or not
	for (int i = 2; i < nthRoot; i++) {
		if (primes[i]) {
			int add = 0;
			for (int j = 0; j < PRIME_LIMIT; j = (int)pow(i, 2) + add * i, add++) {
				primes[j] = false;
			}
		}
	}
	int count = 0;
	//Read contents of array and print Primes
	for (int i = 1; i < sizeof(primes); i++) {
		if (primes[i]) {
			std::cout << "Prime Number: " << i << std::endl;
			count++;
		}
	}

	std::cout << "Total Number of Primes in " << PRIME_LIMIT << " : " << count << std::endl;

    return 0;
}

