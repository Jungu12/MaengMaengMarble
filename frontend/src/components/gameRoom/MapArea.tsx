import { useCallback } from 'react';

type Props = {
  height?: number;
  width?: number;
  src: string;
  alt: string;
  value: number;
  onClickArea?: (landId: number) => void;
};

const MapArea = ({ height, width, src, alt, value, onClickArea }: Props) => {
  const onClick = useCallback(() => {
    onClickArea ? onClickArea(value) : {};
    console.log(value);
  }, [onClickArea, value]);

  return (
    <button onClick={onClick} className='relative'>
      <img
        src={src}
        alt={alt}
        style={{
          width: `${width}px`,
          height: `${height}px`,
        }}
      />
      <img className='absolute top-0 left-0' src={''} alt={''} />
    </button>
  );
};

export default MapArea;
