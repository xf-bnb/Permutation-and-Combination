#pragma once

template<typename _FuncT>
unsigned int Permutation(unsigned int _size, unsigned int _n, _FuncT pfunc)
{
	if (_n > _size)
		_n = _size;

	if (_n == 0)
		return pfunc(nullptr, 0) ? 1 : 0;

	unsigned int _count = 0;

	_simple_fix_stack _sfs{ _n };

	do
	{
		for (unsigned int _pos = 0; _pos < _size; ++_pos)
		{
			if (!!!_sfs.existing(_pos))
			{
				_sfs.push(_pos);
				break;
			}
		}

		while (_sfs.is_full())
		{
			if (pfunc(_sfs.data(), _sfs.index()))
				++_count;

			for (unsigned int _pos = _size; _size <= _pos && !_sfs.is_empty(); )
			{
				_pos = _sfs.pop();

				for (++_pos; _pos < _size; ++_pos)
				{
					if (!!!_sfs.existing(_pos))
					{
						_sfs.push(_pos);
						break;
					}
				}
			}
		}
	} while (!_sfs.is_empty());

	return _count;
}
