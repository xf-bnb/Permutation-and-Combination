// combination.cpp : 此文件包含 "main" 函数。程序执行将在此处开始并结束。
//

#include <iostream>

#include "simple_stack.h"
#include "permutation.h"
#include "combination.h"

template<typename T>
bool Show(const T& k, const unsigned int* arr, unsigned int n)
{
	for (unsigned int i = 0; i < n; ++i)
	{
		std::cout << " " << k[arr[i]];
	}

	std::cout << std::endl;

    return true;
}

int main()
{
    int unique[] = { 1, 2, 3, 4, 5, 0, 9, 8, 7 };
	int repeat[] = { 1, 2, 3, 4, 5, 2, 7 ,7, 2 };

    /*
    unsigned int p = Permutation(4, 4, [&unique](const unsigned int* arr, unsigned int n) { return Show(unique, arr, n); });
    std::cout << "Permutation: " << p << std::endl;
    std::cout << "------------------------------------" << std::endl << std::endl;

    unsigned int c = Combination(5, 3, [&unique](const unsigned int* arr, unsigned int n) { return Show(unique, arr, n); });
    std::cout << "Combination: " << c << std::endl;
    std::cout << "------------------------------------" << std::endl << std::endl;
    */






    unsigned int arr[] = { 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4 };
    unsigned int len = sizeof(arr) / sizeof(unsigned int);

    time_t t1 = clock();

    unsigned int x = Combination([](const unsigned int* s, const unsigned int* c, unsigned int t, unsigned int n) {

        for (_index_t i = 0, j = 0; i < n; ++i)
        {
            if (i == s[j])
            {
                std::cout << c[j++] << " ";
            }
            else
            {
                std::cout << "0 ";
            }
        }

        std::cout << std::endl;

    }, MaxJ, arr, MaxI);












	return 0;
}

